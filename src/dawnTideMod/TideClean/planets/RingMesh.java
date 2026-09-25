package dawnTideMod.TideClean.planets;

import arc.graphics.Color;
import arc.graphics.Gl;
import arc.graphics.Mesh;
import arc.graphics.VertexAttribute;
import arc.math.Mathf;
import arc.math.geom.Mat3D;
import arc.math.geom.Vec3;
import arc.util.Time;
import arc.util.noise.Simplex;
import mindustry.graphics.Shaders;
import mindustry.graphics.g3d.GenericMesh;
import mindustry.graphics.g3d.PlanetMesh;
import mindustry.graphics.g3d.PlanetParams;
import mindustry.type.Planet;

public class RingMesh extends PlanetMesh{

    private static final Mat3D mat = new Mat3D();

    public final int divisions;

    public int rings = 4;

    public float innerRadius;

    public float outerRadius;

    public float tilt;

    public float rotateSpeed = 0f;

    public float bandScale = 12f;

    public float gapThreshold = -0.3f;

    public long seed = 0L;

    public float baseAlpha = 0.72f;

    public float waveHeight = 0.035f;

    public final Color innerColor = new Color();

    public final Color outerColor = new Color();

    public final Color glowColor = new Color();

    public RingMesh(Planet planet, int divisions, float innerRadius, float outerRadius, float tilt,
                    Color innerColor, Color outerColor, Color glowColor){
        this.planet = planet;
        this.shader = Shaders.clouds;

        this.divisions = Math.max(16, divisions);
        this.innerRadius = innerRadius;
        this.outerRadius = outerRadius;
        this.tilt = tilt;

        this.innerColor.set(innerColor);
        this.outerColor.set(outerColor);
        this.glowColor.set(glowColor);

        this.mesh = build();
    }

    private Mesh build(){
        int segs = divisions;
        int ringCount = Math.max(2, rings);
        int vertexCount = (segs + 1) * (ringCount + 1);
        int indexCount = segs * ringCount * 6;

        Mesh result = new Mesh(true, vertexCount, indexCount,
            VertexAttribute.position3, VertexAttribute.normal, VertexAttribute.color);

        result.getVerticesBuffer().limit(result.getVerticesBuffer().capacity());
        result.getVerticesBuffer().position(0);
        result.getIndicesBuffer().limit(result.getIndicesBuffer().capacity());
        result.getIndicesBuffer().position(0);

        float baseRadius = planet.radius;
        float midRadius = (innerRadius + outerRadius) * 0.5f * baseRadius;
        float halfWidth = (outerRadius - innerRadius) * 0.5f * baseRadius;

        float[] floats = new float[7];
        Color color = new Color();

        for(int i = 0; i <= segs; i++){
            float angle = i / (float)segs * 360f;
            float cos = Mathf.cosDeg(angle);
            float sin = Mathf.sinDeg(angle);

            float midNoise = noise(seed + 11, 3, 0.55f, bandScale, cos, sin);
            float widthNoise = noise(seed + 47, 2, 0.50f, bandScale * 0.55f, cos, sin);
            float glowNoise = noise(seed + 91, 3, 0.50f, bandScale * 1.6f, cos, sin);

            float center = midRadius * (1f + midNoise * 0.09f);
            float width = halfWidth * (0.55f + 0.75f * (widthNoise * 0.5f + 0.5f));
            float yOffset = midNoise * baseRadius * waveHeight;

            float gapMul = Mathf.clamp((widthNoise - gapThreshold) * 2.2f, 0f, 1f);

            for(int j = 0; j <= ringCount; j++){
                float t = j / (float)ringCount;
                float radius = center + (t - 0.5f) * 2f * width;

                float edge = Mathf.clamp(Math.min(t, 1f - t) * 4f, 0f, 1f);
                float alpha = baseAlpha * gapMul * (0.35f + 0.65f * edge)
                    * (0.55f + 0.45f * (glowNoise * 0.5f + 0.5f));

                color.set(innerColor).lerp(outerColor, t);
                if(glowNoise > 0f){
                    color.lerp(glowColor, Mathf.clamp(glowNoise) * 0.35f);
                }
                color.a = alpha;

                floats[0] = cos * radius;
                floats[1] = yOffset;
                floats[2] = sin * radius;

                floats[3] = 0f;
                floats[4] = 1f;
                floats[5] = 0f;
                floats[6] = color.toFloatBits();

                result.getVerticesBuffer().put(floats);
            }
        }

        short[] indices = new short[indexCount];
        int p = 0;
        for(int i = 0; i < segs; i++){
            for(int j = 0; j < ringCount; j++){
                int a = i * (ringCount + 1) + j;
                int b = a + (ringCount + 1);

                indices[p++] = (short)a;
                indices[p++] = (short)b;
                indices[p++] = (short)(a + 1);

                indices[p++] = (short)b;
                indices[p++] = (short)(b + 1);
                indices[p++] = (short)(a + 1);
            }
        }
        result.getIndicesBuffer().put(indices);

        result.getVerticesBuffer().limit(result.getVerticesBuffer().position());
        result.getIndicesBuffer().limit(result.getIndicesBuffer().position());
        return result;
    }

    private float noise(long s, int octaves, float persistence, float scale, float cos, float sin){
        int seedValue = (int)(s & 0x7fffffff);
        return Simplex.noise2d(seedValue, octaves, persistence, 1f / Math.max(1f, scale), cos, sin);
    }

    public float rotationOffset(){
        return rotateSpeed == 0f ? 0f : Time.globalTime * rotateSpeed / 40f;
    }

    @Override
    public void preRender(PlanetParams params){
        Shaders.clouds.planet = planet;
        Shaders.clouds.lightDir.set(planet.solarSystem.position).sub(planet.position)
            .rotate(Vec3.Y, planet.getRotation()).nor();
        Shaders.clouds.ambientColor.set(planet.solarSystem.lightColor);

        Shaders.clouds.alpha = 1f;
    }

    @Override
    public void render(PlanetParams params, Mat3D projection, Mat3D transform){
        if(mesh == null || mesh.isDisposed()) return;

        preRender(params);
        shader.bind();
        shader.setUniformMatrix4("u_proj", projection.val);
        shader.setUniformMatrix4("u_trans", mat.setToTranslation(planet.position)
            .rotate(Vec3.Y, planet.getRotation() + rotationOffset())
            .rotate(Vec3.X, tilt).val);
        shader.apply();

        mesh.render(shader, Gl.triangles);
    }

    @Override
    public void dispose(){
        if(mesh != null){
            mesh.dispose();
            mesh = null;
        }
    }

    public GenericMesh asMesh(){
        return this;
    }
}
