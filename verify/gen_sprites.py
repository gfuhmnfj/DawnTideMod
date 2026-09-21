"""曙光潮涌 · 占位贴图生成器（Python + Pillow）

用途：扫描 src 下所有方块/单位构造，找出 assets/sprites 里缺失的贴图，
按方块 size 生成对应像素尺寸的程序化占位图。

用法：
    python gen_sprites.py           # 只生成缺失的
    python gen_sprites.py --dry     # 只列清单，不写文件
"""

import hashlib
import math
import os
import re
import sys

from PIL import Image, ImageDraw

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
SRC = os.path.join(ROOT, "src")
SPRITES = os.path.join(ROOT, "assets", "sprites")
OUT_BLOCKS = os.path.join(SPRITES, "blocks", "gen")
OUT_UNITS = os.path.join(SPRITES, "units", "gen")

TILE = 32

CONSTRUCT = re.compile(r'new\s+([\w.]+)\s*\(\s*"([^"]+)"\s*\)\s*\{\{')
SIZE = re.compile(r'\bsize\s*=\s*(\d+)')
BLOCK_COMMENT = re.compile(r'/\*.*?\*/', re.S)
LINE_COMMENT = re.compile(r'//[^\n]*')
ANIMATED = re.compile(r'-\d+(-\d+)?$')
SKIP_CLASSES = {
    "RegionPart", "DrawTurret", "DrawPart", "PartMove", "PartProgress",
    "Recipe", "IOEntry", "JsonRecipe", "DrawRecipe", "ItemStack", "LiquidStack",
}

UNIT_TYPES = ("SegmentUnitType", "UnitType", "BlockUnitType")

BG = (18, 42, 44, 255)
EDGE = (95, 224, 192, 255)
CORE = (255, 205, 120, 255)
HOT = (255, 120, 90, 255)
COLD = (130, 200, 255, 255)


def hue_color(name):
    digest = hashlib.md5(name.encode("utf-8")).hexdigest()
    base = int(digest[:6], 16)
    r = 40 + (base >> 16 & 0xFF) % 90
    g = 70 + (base >> 8 & 0xFF) % 120
    b = 70 + (base & 0xFF) % 110
    return (r, g, b, 255)


def existing_regions():
    found = set()
    for folder, _, files in os.walk(SPRITES):
        for f in files:
            if not f.lower().endswith(".png"):
                continue
            base = os.path.splitext(f)[0]
            found.add(base.lower())
            found.add(ANIMATED.sub("", base).lower())
    return found


def strip_comments(text):
    text = BLOCK_COMMENT.sub(" ", text)
    return LINE_COMMENT.sub(" ", text)


def scan():
    """@return [(name, size, kind)]"""
    out = []
    for folder, _, files in os.walk(SRC):
        for f in files:
            if not f.endswith(".java"):
                continue
            path = os.path.join(folder, f)
            text = strip_comments(open(path, encoding="utf-8", errors="replace").read())
            for m in CONSTRUCT.finditer(text):
                cls, name = m.group(1), m.group(2)
                if cls.split(".")[-1] in SKIP_CLASSES:
                    continue
                if not name or name.startswith("-") or name.endswith("-"):
                    continue
                body = text[m.end():m.end() + 1800]
                size_match = SIZE.search(body)
                size = int(size_match.group(1)) if size_match else 1
                kind = "unit" if cls.endswith(UNIT_TYPES) else "block"
                out.append((name, size, kind))
    seen = {}
    for name, size, kind in out:
        seen[name.lower()] = (name, size, kind)
    return list(seen.values())


def draw_block(name, size, path):
    px = max(TILE, TILE * size)
    img = Image.new("RGBA", (px, px), (0, 0, 0, 0))
    d = ImageDraw.Draw(img)

    fill = hue_color(name)
    if "core" in name:
        fill = CORE
    elif "heat" in name or "thermal" in name:
        fill = HOT
    elif "cryo" in name or "cool" in name:
        fill = COLD

    pad = max(1, px // 16)
    d.rounded_rectangle([pad, pad, px - 1 - pad, px - 1 - pad],
                        radius=max(2, px // 10), fill=fill, outline=EDGE, width=max(1, px // 32))

    inner = Image.new("RGBA", (px, px), (0, 0, 0, 0))
    di = ImageDraw.Draw(inner)
    step = max(4, px // 6)
    for i in range(step, px - pad, step):
        di.line([(pad + 1, i), (px - pad - 2, i)], fill=(255, 255, 255, 26), width=1)
        di.line([(i, pad + 1), (i, px - pad - 2)], fill=(255, 255, 255, 26), width=1)
    img.alpha_composite(inner)

    r = max(2, px // 12)
    cx = cy = px // 2
    d.ellipse([cx - r, cy - r, cx + r, cy + r], fill=BG, outline=EDGE, width=max(1, px // 32))

    img.save(path)


def draw_unit(name, size, path):
    px = max(24, TILE * 2)
    img = Image.new("RGBA", (px, px), (0, 0, 0, 0))
    d = ImageDraw.Draw(img)
    fill = hue_color(name)
    d.ellipse([2, px // 4, px - 3, px - px // 4], fill=fill, outline=EDGE, width=2)
    d.ellipse([px // 2 - 4, px // 2 - 4, px // 2 + 4, px // 2 + 4], fill=BG, outline=CORE, width=2)
    img.save(path)


def draw_extra():
    """炮塔部件：prism 的 blade / inner / mid 及其 heat 版。"""
    px = 128
    made = []
    for part in ("blade", "inner", "mid"):
        img = Image.new("RGBA", (px, px), (0, 0, 0, 0))
        d = ImageDraw.Draw(img)
        d.rounded_rectangle([px // 2 - 12, 10, px // 2 + 12, px - 24],
                            radius=6, fill=(196, 118, 62, 255), outline=EDGE, width=2)
        d.ellipse([px // 2 - 6, 14, px // 2 + 6, 30], fill=(255, 246, 220, 255))
        img.save(os.path.join(OUT_BLOCKS, f"prism-{part}.png"))
        made.append(f"prism-{part}.png")

        heat = Image.new("RGBA", (px, px), (0, 0, 0, 0))
        dh = ImageDraw.Draw(heat)
        dh.ellipse([px // 2 - 9, 16, px // 2 + 9, 34], fill=(255, 98, 20, 190))
        heat.save(os.path.join(OUT_BLOCKS, f"prism-{part}-heat.png"))
        made.append(f"prism-{part}-heat.png")
    return made


def main():
    dry = "--dry" in sys.argv

    have = existing_regions()
    todo = []

    for name, size, kind in sorted(scan()):
        if name.lower() in have:
            continue
        todo.append((name, size, kind))

    print(f"缺失贴图 {len(todo)} 个：")
    for name, size, kind in todo:
        print(f"  {kind:5s} {name}  ({TILE * size}x{TILE * size})")

    if dry:
        return

    os.makedirs(OUT_BLOCKS, exist_ok=True)
    os.makedirs(OUT_UNITS, exist_ok=True)

    for name, size, kind in todo:
        path = os.path.join(OUT_UNITS if kind == "unit" else OUT_BLOCKS, f"{name}.png")
        if kind == "unit":
            draw_unit(name, size, path)
        else:
            draw_block(name, size, path)

    extra = draw_extra()
    print(f"炮塔部件 {len(extra)} 个：{', '.join(extra)}")
    print(f"输出目录：{OUT_BLOCKS} / {OUT_UNITS}")


if __name__ == "__main__":
    main()
