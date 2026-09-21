"""剥离 Java 源码注释（保留字符串字面量内的 // 与 /*）。

用法：
    python strip_comments.py --dry
    python strip_comments.py
"""

import os
import re
import sys

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
SRC = os.path.join(ROOT, "src")

KEEP = {"DawnTideBlocks.java", "DawnTideItems.java", "DawnTideLiquids.java"}

IMPORT = re.compile(r'^import\s+(?:static\s+)?([\w.]+)\s*;\s*$')


def strip(text):
    out = []
    i = 0
    n = len(text)
    state = None  # None | '"' | "'"
    while i < n:
        c = text[i]
        nxt = text[i + 1] if i + 1 < n else ""

        if state is None:
            if c == '"' or c == "'":
                state = c
                out.append(c)
                i += 1
                continue
            if c == "/" and nxt == "/":
                while i < n and text[i] != "\n":
                    i += 1
                continue
            if c == "/" and nxt == "*":
                i += 2
                while i + 1 < n and not (text[i] == "*" and text[i + 1] == "/"):
                    i += 1
                i += 2
                continue
            out.append(c)
            i += 1
        else:
            out.append(c)
            if c == "\\":
                if i + 1 < n:
                    out.append(nxt)
                    i += 2
                    continue
            elif c == state:
                state = None
            i += 1

    text = "".join(out)
    text = "\n".join(line.rstrip() for line in text.splitlines())
    text = re.sub(r"\n{3,}", "\n\n", text)
    return text.strip("\n") + "\n"


def clean_imports(text):
    """去掉正文未引用的 import，保留所有 static import。"""
    lines = text.splitlines()
    used = "\n".join(l for l in lines if not l.startswith("import "))
    kept, removed = [], []
    for line in lines:
        m = IMPORT.match(line)
        if m and not line.strip().startswith("import static"):
            simple = m.group(1).rsplit(".", 1)[-1]
            if simple != "*" and not re.search(r"\b" + re.escape(simple) + r"\b", used):
                removed.append(simple)
                continue
        kept.append(line)
    return "\n".join(kept) + "\n", removed


def main():
    dry = "--dry" in sys.argv
    changed = 0
    for folder, _, files in os.walk(SRC):
        for name in files:
            if not name.endswith(".java") or name in KEEP:
                continue
            path = os.path.join(folder, name)
            original = open(path, encoding="utf-8").read()
            text = strip(original)
            text, removed = clean_imports(text)
            if text == original:
                continue
            changed += 1
            print(f"{name}: {len(original)} -> {len(text)} 字节"
                  + (f"，删 import {len(removed)}" if removed else ""))
            if not dry:
                open(path, "w", encoding="utf-8", newline="\n").write(text)
    print(f"共处理 {changed} 个文件（保留 {', '.join(sorted(KEEP))} 的注释）")


if __name__ == "__main__":
    main()
