//package graphics;
//
//import static org.lwjgl.opengl.GL11.glGetInteger;
//import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
//import static org.lwjgl.opengl.GL13.glActiveTexture;
//import static org.lwjgl.opengl.GL20.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS;
//
//public class TU
//{
//    private static int ACTIVE_UNIT;
//
//    public static int getActiveUnit()
//    {
//        return ACTIVE_UNIT;
//    }
//
//[]    {
//        if (textureUnit == ACTIVE_UNIT)
//        {
//            return;
//        }
//
//        if(0 <= textureUnit && textureUnit <=  glGetInteger(GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS))
//        {
//            glActiveTexture(GL_TEXTURE0 + textureUnit);
//
//            ACTIVE_UNIT = textureUnit;
//        }
//    }
//}
