// This is a OpenGL ES 1.0 dynamic font rendering system. It loads actual font
// files, generates a font map (texture) from them, and allows rendering of
// text strings.
//
// NOTE: the rendering portions of this class uses a sprite batcher in order
// provide decent speed rendering. Also, rendering assumes a BOTTOM-LEFT
// origin, and the (x,y) positions are relative to that, as well as the
// bottom-left of the string to render.

package uk.co.blogspot.fractiousg.texample;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.opengl.GLUtils;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.microedition.khronos.opengles.GL10;

public class GLText {
    private final static String TAG = "Print-GLText";
    //--Constants--//
    public final static int CHAR_START = 32;           // First Character (ASCII Code)
    public final static int CHAR_END = 126;            // Last Character (ASCII Code)
    public final static int CHAR_CNT = (((CHAR_END - CHAR_START) + 1) + 1);  // Character Count (Including Character to use for Unknown)

    public final static int CHAR_NONE = 32;            // Character to Use for Unknown (ASCII Code)
    public final static int CHAR_UNKNOWN = (CHAR_CNT - 1);  // Index of the Unknown Character
    // sxg start
    public static int CHAR_UNKNOWN_Strs = -1;  // Index of the Unknown Character
    // sxg end

    public final static int FONT_SIZE_MIN = 6;         // Minumum Font Size (Pixels)
    public final static int FONT_SIZE_MAX = 180;       // Maximum Font Size (Pixels)

    public final static int CHAR_BATCH_SIZE = 100;     // Number of Characters to Render Per Batch

    /**
     * 第一次渲染
     */
    private boolean isFirstRender = true;
    private boolean reload;

    //--Members--//
    GL10 gl;                                           // GL10 Instance
    AssetManager assets;                               // Asset Manager
    SpriteBatch batch;                                 // Batch Renderer

    int fontPadX, fontPadY;                            // Font Padding (Pixels; On Each Side, ie. Doubled on Both X+Y Axis)
    /** 字的起始点 */
    float fontStartX, fontStartY;

    float fontHeight;                                  // Font Height (Actual; Pixels)
    float fontAscent;                                  // Font Ascent (Above Baseline; Pixels)
    float fontDescent;                                 // Font Descent (Below Baseline; Pixels)

    int textureId;                                     // Font Texture ID [NOTE: Public for Testing Purposes Only!]
    int textureSize;                                   // Texture Size for Font (Square) [NOTE: Public for Testing Purposes Only!]
    TextureRegion textureRgn;                          // Full Texture Region

    float charWidthMax;                                // Character Width (Maximum; Pixels)
    float charHeight;                                  // Character Height (Maximum; Pixels)
//    final float[] charWidths;                          // Width of Each Character (Actual; Pixels)
    TextureRegion[] charRgn;                           // Region of Each Character (Texture Coordinates)
    private int[] textureIds = null;
    private final Object mutex;

    /**
     * 字模板里每个字的宽度
     */
    private List<Integer> charWidthList;
    /**
     * 字模板列表
     */
    private List<TextureRegion> charRgnList;
    int cellWidth, cellHeight;                         // Character Cell Width/Height
    int rowCnt, colCnt;                                // Number of Rows/Columns

    float scaleX, scaleY;                              // Font Scale (X,Y Axis)
    float spaceX;                                      // Additional (X,Y Axis) Spacing (Unscaled)
    double zoom = 0;


    // sxg start
    String[] strs = null;// Array Counter
    List<String> strList = null;// Array Counter
    // sxg end

    //--Constructor--//
    // D: save GL instance + asset manager, create arrays, and initialize the members
    // A: gl - OpenGL ES 10 Instance
//    public GLText(GL10 gl, AssetManager assets, Context context, Typeface tf, int size, int padX, int padY, List<String> fontList) {
    public GLText(GL10 gl, AssetManager assets, Context context, Typeface tf, int size, int padX, int padY, double zoom, List<String> strListParam) {
        Log.i(TAG, "GLText: ");
        this.gl = gl;                                   // Save the GL10 Instance
        this.assets = assets;                           // Save the Asset Manager Instance

        strList = new ArrayList<String>();

        strs = new String[]{"*",
                "办", // B
                "仓", "厕", "次","充","厨",
                "大","电", "点",
                "房",
                "公", //G
                "会",
                "间", "讲","客", "口","库",
                "廊", "楼",
                "门",
                "起",  "前", "区", //Q
                "生", "室","所",
                "台", "堂","梯", "厅",
                "卫","卧","屋",
                "阳","议",
                "主",  "桩", "走"};
        for (String str : strs) {
            strList.add(str);
        }
        if (strListParam != null && strListParam.size() > 0) {
            for (int i = 0; i < strListParam.size(); i++) {
                strList.add(strListParam.get(i));
            }
        }



//       strList = initCN();

//       strList = fontList;

        CHAR_UNKNOWN_Strs = strs.length - 1;
        batch = new SpriteBatch(gl, CHAR_BATCH_SIZE);  // Create Sprite Batch (with Defined Size)

//       // 原 start
//      charWidths = new float[CHAR_CNT];               // Create the Array of Character Widths
//      charRgn = new TextureRegion[CHAR_CNT];          // Create the Array of Character Regions
//       // 原 end
        // sxg start
        isFirstRender = true;
//        charWidths = new float[strs.length];
        charRgn = new TextureRegion[strs.length];

        charWidthList = new ArrayList<Integer>();
        charRgnList = new ArrayList<TextureRegion>();
        // sxg end

        // initialize remaining members
        fontPadX = 0;
        fontPadY = 0;

        fontHeight = 0.0f;
        fontAscent = 0.0f;
        fontDescent = 0.0f;

        textureId = -1;
        textureSize = 0;

        charWidthMax = 0;
        charHeight = 0;

        cellWidth = 0;
        cellHeight = 0;
        rowCnt = 0;
        colCnt = 0;

        scaleX = 1.0f;                                  // Default Scale = 1 (Unscaled)
        scaleY = 1.0f;                                  // Default Scale = 1 (Unscaled)
        spaceX = 0.0f;

        mutex = new Object();
        reload = true;

        this.tf = tf;
        this.textSize = size;
        this.padX = padX;
        this.padY = padY;
        this.context = context;

        this.zoom = zoom;
    }

    public boolean load(String file, int size, int padX, int padY, List<String> strListRender) {
        Typeface tf = Typeface.createFromAsset(assets, file);  // Create the Typeface from Font File
        return load(null, tf, size, padX, padY, strListRender);
    }

    private Paint paint;
    private Bitmap bitmap;
    private Canvas canvas;

    private Typeface tf = null;
    private int textSize = -1;
    private int padX = -1;
    private int padY = -1;
    private Context context;

    public void setStrList(List<String> strListParam) {
        if (strListParam == null) {
            Log.i(TAG, "setStrList: strList is null, return!");
            return;
        }
        Log.i(TAG, "setStrList: this.strList.size()=" + this.strList.size() + ", 新strList.size()" + strListParam.size());
        this.strList = strListParam;
    }

    int offsetX = 0;// 12;
    int offsetY = 0;// 44;


    //--Load Font--//
    // description
    //    this will load the specified font file, create a texture for the defined
    //    character range, and setup all required values used to render with it.
    // arguments:
    //    file - Filename of the font (.ttf, .otf) to use. In 'Assets' folder.
    //    size - Requested pixel size of font (height)
    //    padX, padY - Extra padding per character (X+Y Axis); to prevent overlapping characters.
    public boolean load(Context context, Typeface tf, int size, int padX, int padY, List<String> strListRender){
        return false;
    }
    public boolean loadModule() {
        if(!reload){
            return true;
        }
       Log.i(TAG, "load: strList.size()=" + strList.size());
      // setup requested values
      fontPadX = padX;                                // Set Requested X Axis Padding
      fontPadY = padY;                                // Set Requested Y Axis Padding

      // setup paint instance for drawing
      Paint paint = new Paint();                      // Create Android Paint Instance
      paint = new Paint();                      // Create Android Paint Instance
      paint.setAntiAlias( true );                     // Enable Anti Alias
      paint.setTextSize( textSize  );                      // Set Text Size
      paint.setColor( 0xffffffff );     //0xffffffff               // Set ARGB (White, Opaque)
      paint.setTypeface( tf );                        // Set Typeface

      // get font metrics,fontHeight= , fontDescent=11.0
      Paint.FontMetrics fm = paint.getFontMetrics();  // Get Font Metrics
      fontHeight = (float)Math.ceil( Math.abs( fm.bottom ) + Math.abs( fm.top ) );  // Calculate Font Height
      fontAscent = (float)Math.ceil( Math.abs( fm.ascent ) );  // Save Font Ascent
      fontDescent = (float)Math.ceil( Math.abs( fm.descent ) );  // Save Font Descent

       // sxg start
       String sCN = "";                      // Create Character Array
       charWidthMax = charHeight = 0;                  // Reset Character Width/Height Maximums
       float[] wCN = new float[2];                       // Working Width Value
       int cntCN = 0;
       if(charWidthList.size() > 0){
           charWidthList.clear();
           Log.i(TAG, "load: 已清理charWidthList，charWidthList.size()=" + charWidthList.size());
       }

       int sumWidth = 0;
//       Log.i(TAG, "load: 字的宽度-------------------------------------");
       for ( int i = 0; i < strList.size(); i++ )  {  // FOR Each Character
//       for ( int i = 0; i < strListRender.size(); i++ )  {  // FOR Each Character
           sCN = strList.get(i);                                    // Set Character
//           sCN = strListRender.get(i);                                    // Set Character
           paint.getTextWidths(sCN, 0, 1, wCN);
           sumWidth += (int)wCN[0];
           charWidthList.add((int)wCN[0]);                      // Get Width
           if ( wCN[0] > charWidthMax )        // IF Width Larger Than Max Width
               charWidthMax = wCN[0];           // Save New Max Width
           cntCN++;                                       // Advance Array Counter
       }
//       Log.i(TAG, "字的宽度-------------------------------------sumWidth=" + sumWidth);
       Log.i(TAG, "load: charWidthList.size()=" + charWidthList.size());
//       sCN = strList.get(0);                     // Set Unknown Character
//       paint.getTextWidths(sCN, 0, 1, wCN);           // Get Character Bounds
////       charWidths[cntCN] = wCN[0];                         // Get Width
//       charWidthList.set(0, (int)wCN[0]);
//       if ( charWidthList.get(0) > charWidthMax )           // IF Width Larger Than Max Width
//           charWidthMax = charWidthList.get(0);              // Save New Max Width
//       cntCN++;
       // sxg end

      // set character height to font height
      charHeight = fontHeight;                        // Set Character Height

      // find the maximum size, validate, and setup cell sizes
      cellWidth = (int)charWidthMax + ( 2 * fontPadX );  // Set Cell Width
      cellHeight = (int)charHeight + ( 2 * fontPadY );  // Set Cell Height
//       cellWidth = (int)charWidthMax + (fontPadX / 2);  // Set Cell Width
//       cellHeight = (int)charHeight + (fontPadY / 2);  // Set Cell Height
      int maxSize = cellWidth > cellHeight ? cellWidth : cellHeight;  // Save Max Size (Width/Height)
//       Log.i(TAG, "maxSize="+maxSize+", FONT_SIZE_MIN=" + FONT_SIZE_MIN+",FONT_SIZE_MAX="+FONT_SIZE_MAX);
      if ( maxSize < FONT_SIZE_MIN || maxSize > FONT_SIZE_MAX )  // IF Maximum Size Outside Valid Bounds
         return false;                                // Return Error

      // set texture size based on max font size (width or height)
      // NOTE: these values are fixed, based on the defined characters. when
      // changing start/end characters (CHAR_START/CHAR_END) this will need adjustment too!
           if ( maxSize <= 24 )                            // IF Max Size is 18 or Less
               textureSize = 256;                           // Set 256 Texture Size
           else if ( maxSize <= 40 )                       // ELSE IF Max Size is 40 or Less
               textureSize = 512;                           // Set 512 Texture Size
           else if ( maxSize <= 80 )                       // ELSE IF Max Size is 80 or Less
               textureSize = 1024;                          // Set 1024 Texture Size
           else
               textureSize = 2048; // 4096

       // Set 2048 Texture Size
//       Log.i(TAG, "load: maxSize=" + maxSize + " textureSize=" + textureSize); // 看两次textureSize是否相同 textureSize=2048,textureSize=2048 两次相同

//       List<Bitmap> listBitmap = new ArrayList<>();
      // create an empty bitmap (alpha only)
      Bitmap bitmap = Bitmap.createBitmap( textureSize, textureSize, Bitmap.Config.ARGB_8888 );  // Create Bitmap//原：ALPHA_8 ARGB_8888
      Canvas canvas = new Canvas( bitmap );           // Create Canvas for Rendering to Bitmap
      bitmap.eraseColor( 0 );                // Set Transparent Background (ARGB)

      // calculate rows/columns
      // NOTE: while not required for anything, these may be useful to have :)
      colCnt = textureSize / cellWidth;               // Calculate Number of Columns
//       // 原 start
//      rowCnt = (int)Math.ceil( (float)CHAR_CNT / (float)colCnt );  // Calculate Number of Rows
//       // 原 end

       // sxg start
//       rowCnt = (int)Math.ceil( (float)strs.length / (float)colCnt );  // Calculate Number of Rows
       rowCnt = (int)Math.ceil( (float)strList.size() / (float)colCnt );  // Calculate Number of Rows

    // sxg start
       float x = fontPadX;
       float y = (cellHeight - 1) - fontDescent - fontPadY;
//        Log.i(TAG, "loadModule: 第一个字的x,y=(" + x + ", " + y + ")");
        fontStartX = x;
        fontStartY = y;
       for (int i = 0; i < strList.size(); i++) {
//       for (int i = 0; i < strListRender.size(); i++) {
           sCN = strList.get(i);
//           sCN = strListRender.get(i);
           canvas.drawText(sCN, x, y, paint);
           x += cellWidth;
           if ((x + cellWidth - fontPadX) > textureSize) {
               x = fontPadX;
               y += cellHeight;
           }
       }
//       Log.i(TAG, "load: 如果是第一次渲染，就画未知字符，否则不画,textureId=" + textureId);
       if (textureId < 0) {
           sCN = strList.get(0);                               // Set Character to Use for NONE，不加这个，星号画不出来,为什么
           canvas.drawText(sCN, x, y, paint); // 这估计就是最后一个*
           if ((x + cellWidth - fontPadX) > textureSize) {
               x = fontPadX;
               y += cellHeight;
           }
           offsetX = (int)x;
           offsetY = (int)y;
       }

       // sxt end


       // generate a new texture
//       int[] textureIds = new int[1];                  // Array to Get Texture Id
        if (textureIds == null) {
            textureIds = new int[1];
            gl.glGenTextures(1, textureIds, 0); // invalid value
            textureId = textureIds[0];                      // Save Texture Id
        }
       // setup filters for texture
       gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);  // Bind Texture

       gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST );  // Set Minification Filter
       gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR );  // Set Magnification Filter
       gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);  // Set U Wrapping
       gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);  // Set V Wrapping

//        // 定义矩阵对象
//        Matrix matrix = new Matrix();
////        // 缩放原图
////        matrix.postScale(1f, 1f);
//        // 向左旋转45度，参数为正则向右旋转
//        matrix.postRotate(180);
//        //bmp.getWidth(), 500分别表示重绘后的位图宽高
//        Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
//                matrix, true);

        // load the generated bitmap onto the texture
       GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);  // Load Bitmap to Texture
//       GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, dstbmp, 0);  // Load Bitmap to Texture
       gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);      // Unbind Texture invalid operation
//       Log.i(TAG, "load: 生成纹理id,textureId=" + textureId);
//       bind(gl, bitmap);

       // 保存bitmap
       String savePath = saveBitmap(context, bitmap);
       Log.i(TAG, "load: savePath=" + savePath);
      // release the bitmap
      bitmap.recycle();                               // Release the Bitmap
       bitmap = null;

//       // 原 start
//      // setup the array of character texture regions
//      x = 0;                                          // Initialize X
//      y = 0;                                          // Initialize Y
////       Log.i(TAG, "textureSize=" + textureSize +", x="+x+", y="+y+", cellWidth-1="+(cellWidth-1) +", cellHeight-1="+(cellHeight-1));
//      for ( int c = 0; c < CHAR_CNT; c++ )  {         // FOR Each Character (On Texture)
//         charRgn[c] = new TextureRegion( textureSize, textureSize, x, y, cellWidth-1, cellHeight-1 );  // Create Region for Character
//         x += cellWidth;                              // Move to Next Char (Cell)
//         if ( x + cellWidth > textureSize )  {
//            x = 0;                                    // Reset X Position to Start
//            y += cellHeight;                          // Move to Next Row (Cell)
//         }
//      }
//       // 原 end

       // sxg start
       // setup the array of character texture regions
       x = 0;                                          // Initialize X
       y = 0;                                          // Initialize Y
       if(charRgnList.size() > 0){
           charRgnList.clear();
           Log.i(TAG, "load: 已清理charRgnList，charRgnList.size()=" + charRgnList.size());
       }
       for ( int i = 0; i < strList.size(); i++ )  {         // FOR Each Character (On Texture)
//       for ( int i = 0; i < strListRender.size(); i++ )  {         // FOR Each Character (On Texture)
           charRgnList.add(new TextureRegion( textureSize, textureSize, x, y, cellWidth-1, cellHeight-1 ));  // Create Region for Character
           x += cellWidth;                              // Move to Next Char (Cell)
           if ( x + cellWidth > textureSize )  {
               x = 0;                                    // Reset X Position to Start
               y += cellHeight;                          // Move to Next Row (Cell)
           }
       }
       Log.i(TAG, "load: charRgnList.size()=" + charRgnList.size());
       // sxg end

      // create full texture region
//       if (textureId < 0) {
           textureRgn = new TextureRegion( textureSize, textureSize, 0, 0, textureSize, textureSize );  // Create Full Texture Region
//       }
//       isFirstRender = false;
      // return success
      return true;                                    // Return Success
   }

    private void bind(GL10 gl, Bitmap bitmapFront) {
        if (textureIds == null) {
            textureIds = new int[1];
            textureId = textureIds[0];
            gl.glGenTextures(1, textureIds, 0);
            reload = true;
        }
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIds[0]);
//        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
//        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);


        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST );  // Set Minification Filter
        gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR );  // Set Magnification Filter
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);  // Set U Wrapping
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);  // Set V Wrapping


        synchronized (mutex) {
            if (reload && bitmapFront != null) {
                GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmapFront, 0);//bitmapFront NullPointerException
                reload = false;
            }
        }
    }

    /**
     * 获取增加的字符数组
     */
    public List<String> getNewStringList(String strsModule) {
        Log.i(TAG, "getNewStringList: strList.size()=" + strList.size());
        List<String> strsParam = new ArrayList<String>();
        String strTemp = "";
        for (int i = 0; i < strsModule.length(); i++) {
            // 取单个字
            if(strsModule.length() > 1) strTemp = strsModule.substring(i, i + 1);
            else strTemp = strsModule.substring(i);
            boolean isExist = false;
            // 如果已存在就不添加
            for (String strExisted : strList) {
                if (strExisted.equals(strTemp)) {
                    isExist = true;
//                    Log.i(TAG, "getNewStringList: 已存在字则不添加字模板：strExisted=" + strExisted + ", strTemp=" + strTemp);
                    break;
                }
            }
            if(!isExist) {
                strsParam.add(strTemp);
//                strList.add(strTemp);  // 把字符加到字符列表里去。
                Log.i(TAG, "getNewStringList: 新字列表strTemp=" + strTemp + ", 所有字列表strList.size()=" + strList.size());
            }
        }
        if (strsParam.size() <= 0) {
            Log.i(TAG, "getNewStringList: 没有需要添加的字模板，不添加");
        }else{
//            charWidthList.clear();
//            charRgnList.clear();
            Log.i(TAG, "getNewStringList: 需要添加的字模板strsParam.size()=" + strsParam.size() + ", strsParam.toString()=" + strsParam.toString()); // 需要添加的字模板strsParam.size()=1, strsParam.toString()=[客厅]
        }
        return strsParam;
    }

//        // sxt end

   //--Begin/End Text Drawing--//
   // D: call these methods before/after (respectively all draw() calls using a text instance
   //    NOTE: color is set on a per-batch basis, and fonts should be 8-bit alpha only!!!
   // A: red, green, blue - RGB values for font (default = 1.0)
   //    alpha - optional alpha value for font (default = 1.0)
   // R: [none]
   public void begin()  {
      begin(1.0f, 1.0f, 1.0f, 1.0f);                // Begin with White Opaque
   }
   public void begin(float alpha)  {
       begin(1.0f, 1.0f, 1.0f, alpha);               // Begin with White (Explicit Alpha)
   }
   public void begin(float red, float green, float blue, float alpha)  {
      gl.glColor4f(red, green, blue, alpha);        // Set Color+Alpha
      gl.glBindTexture( GL10.GL_TEXTURE_2D, textureId );  // Bind the Texture
      batch.beginBatch();                             // Begin Batch
   }
   public void end()  {
      batch.endBatch();                               // End Batch

//       batch.endBatchBeforePush();
//       batch.endBatchDraw();
//       batch.endBatchAfterPop();

      gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);  // Bind the Texture
      gl.glColor4f( 1.0f, 1.0f, 1.0f, 1.0f );         // Restore Default Color/Alpha
   }

    public void endBeforePush() {
        batch.endBatchBeforePush();
    }

    public void endDraw() {
        batch.endBatchDraw();
    }
    public void endAfterPop() {
        batch.endBatchAfterPop();

        gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);  // Bind the Texture
        gl.glColor4f( 1.0f, 1.0f, 1.0f, 1.0f );         // Restore Default Color/Alpha
    }


    /**
     * 获取字在字符列表中的位置
     */
    public void getLocation(String text) {
        int len = text.length();
        int c = -1;
        // sxg start
        for ( int i = 0; i < len; i++ ) {              // FOR Each Character in String
//           int c = (int)text.charAt( i ) - CHAR_START;  // Calculate Character Index (Offset by First Char in Font)
//           int c = text.indexOf(i); // c = -1
            c = -1;
            String strTemp = "";
            if (i < len - 1) strTemp = text.substring(i, i + 1);
            else strTemp = text.substring(i);
//           Log.i(TAG, "draw: strTemp=" + strTemp + ", i=" + i); // 此句刷屏

//           for (int position = 0; position < strs.length; position++) {
            for (int position = 0; position < strList.size(); position++) {
//               if (strTemp.contains(strs[position])) {
                if (strTemp.contains(strList.get(position))) {
                    c = position;
//                    Log.i(TAG, "getLocation: strTemp=" + strTemp + ", c=" + c);
                    break;
                }
            }
        }
    }

   //--Draw Text--//
   // D: draw text at the specified x,y position
   // A: text - the string to draw
   //    x, y - the x,y position to draw text at (bottom left of text; including descent)
   // R: [none]
   public void draw(String text, float x, float y)  {
//       Log.i(TAG, "draw: text=" + text);
//      float chrWidth = cellWidth * scaleX;            // Calculate Scaled Character Width, scaleX = 1.0
//       float chrHeight = cellHeight * scaleY;          // Calculate Scaled Character Height, scaleY = 1.0
       float chrWidth = cellWidth * scaleX;            // Calculate Scaled Character Width, scaleX = 1.0
       float chrHeight = cellHeight * scaleY;          // Calculate Scaled Character Height, scaleY = 1.0
       int len = text.length();                        // Get String Length
      x += ( chrWidth / 2.0f ) - ( fontPadX * scaleX );  // Adjust Start X
      y += ( chrHeight / 2.0f ) - ( fontPadY * scaleY );  // Adjust Start Y

//       if (mCameraMarginWidthHeight != null && mCameraFontWidthHeight != null) { // 默认一个字就在正中间，两个字就需要自己调整
////           x = (float)(x - ((getFontsWidthHeight(text)[0] - getFontsWidthHeight(text.substring(0, 1))[0]) * scaleX / 2));  // Adjust Start X，文字是横向排列的，只有横向上要考虑字的个数。
//           float[] widths = getFontsWidthHeight(text);
//           x = (float)(x - ( (widths[0] - widths[0] / text.length()) * scaleX / 2) );  // Adjust Start X，文字是横向排列的，只有横向上要考虑字的个数。
//           y = (float)(y - (mCameraFontWidthHeight[1] * scaleY / 2));  // Adjust Start Y
//       }
//       else {
//            x += ( chrWidth / 2.0f );  // Adjust Start X
//            y += ( chrHeight / 2.0f );  // Adjust Start Y
//       }

       int c = -1;
       // sxg start
       for ( int i = 0; i < len; i++ )  {              // FOR Each Character in String
//           int c = (int)text.charAt( i ) - CHAR_START;  // Calculate Character Index (Offset by First Char in Font)
//           int c = text.indexOf(i); // c = -1
           c = -1;
           String strTemp = "";
           if(i < len -1) strTemp = text.substring(i, i+1);
           else strTemp = text.substring(i);
//           Log.i(TAG, "draw: strTemp=" + strTemp + ", i=" + i); // 此句刷屏

//           for (int position = 0; position < strs.length; position++) {
           for (int position = 0; position < strList.size(); position++) {
//               if (strTemp.contains(strs[position])) {
               if (strTemp.contains(strList.get(position))) {
                   c = position;
                   break;
               }
           }

//           Log.i(TAG, "draw: strTemp在strList中的位置：" + c); // 此句刷屏
//           if ( c < 0 || c >= strs.length )            // IF Character Not In Font
           if ( c < 0 || c >= strList.size() ) {            // IF Character Not In Font

               c = CHAR_UNKNOWN_Strs;                         // Set to Unknown Character Index
               Log.e(TAG, "draw: 遇到不认识的字不画，strTemp=" + strTemp); // 认识的字也画了*
           }
//               Log.e(TAG, "draw: 遇到不认识的字，不画"); // 不存在的时候不画
//               Log.i(TAG, "draw: 开始画字"); // 能画
//               batch.drawSprite(x, y, chrWidth, chrHeight, charRgn[c]);  // Draw the Character
//           Log.i(TAG, "draw: charRgnList.size()=" + charRgnList.size() + ", charWidthList.size()=" + charWidthList.size());
           batch.drawSprite(x, y, chrWidth, chrHeight, charRgnList.get(c), zoom);  // Draw the Character

//               x += (charWidths[c] + spaceX) * scaleX;    // Advance X Position by Scaled Character Width
           if(c >= 0 && c <= charWidthList.size())    x += (charWidthList.get(c) + spaceX) * scaleX;    // Advance X Position by Scaled Character Width
           else {
               Log.e(TAG, "draw: c=" + c + ", charWidthList.size()=" + charWidthList.size());
           }
//          Log.i(TAG, "c=" + c+", charRgn[c]=" + charRgn[c]);
       }

       // sxg end
   }

   //--Draw Text Centered--//
   // D: draw text CENTERED at the specified x,y position
   // A: text - the string to draw
   //    x, y - the x,y position to draw text at (bottom left of text)
   // R: the total width of the text that was drawn
   public float drawC(String text, float x, float y)  {
      float len = getLength( text );                  // Get Text Length
      draw( text, x - ( len / 2.0f ), y - ( getCharHeight() / 2.0f ) );  // Draw Text Centered
      return len;                                     // Return Length
   }
   public float drawCX(String text, float x, float y)  {
      float len = getLength( text );                  // Get Text Length
      draw( text, x - ( len / 2.0f ), y );            // Draw Text Centered (X-Axis Only)
      return len;                                     // Return Length
   }
   public void drawCY(String text, float x, float y)  {
      draw( text, x, y - ( getCharHeight() / 2.0f ) );  // Draw Text Centered (Y-Axis Only)
   }

   //--Set Scale--//
   // D: set the scaling to use for the font
   // A: scale - uniform scale for both x and y axis scaling
   //    sx, sy - separate x and y axis scaling factors
   // R: [none]
   public void setScale(float scale)  {
      scaleX = scaleY = scale;                        // Set Uniform Scale
   }
   public void setScale(float sx, float sy)  {
      scaleX = sx;                                    // Set X Scale
      scaleY = sy;                                    // Set Y Scale
   }

   //--Get Scale--//
   // D: get the current scaling used for the font
   // A: [none]
   // R: the x/y scale currently used for scale
   public float getScaleX()  {
      return scaleX;                                  // Return X Scale
   }
   public float getScaleY()  {
      return scaleY;                                  // Return Y Scale
   }

   //--Set Space--//
   // D: set the spacing (unscaled; ie. pixel size) to use for the font
   // A: space - space for x axis spacing
   // R: [none]
   public void setSpace(float space)  {
      spaceX = space;                                 // Set Space
   }

    public void setReload(boolean reload) {
        this.reload = reload;
    }

    //--Get Space--//
   // D: get the current spacing used for the font
   // A: [none]
   // R: the x/y space currently used for scale
   public float getSpace()  {
      return spaceX;                                  // Return X Space
   }

   //--Get Length of a String--//
   // D: return the length of the specified string if rendered using current settings
   // A: text - the string to get length for
   // R: the length of the specified string (pixels)
   public float getLength(String text) {
      float len = 0.0f;                               // Working Length
      int strLen = text.length();                     // Get String Length (Characters)
//     // 原 start
//      for ( int i = 0; i < strLen; i++ )  {           // For Each Character in String (Except Last
//         int c = (int)text.charAt( i ) - CHAR_START;  // Calculate Character Index (Offset by First Char in Font)
//         len += ( charWidths[c] * scaleX );           // Add Scaled Character Width to Total Length
//      }
//       // 原 end

      // sxg start
       if (strList.size() != charWidthList.size()) {
           Log.e(TAG, "getLength: strList.size() != charWidthList.size()");
           len = charWidthMax * scaleX * strLen;
       }
       else {
           char[] single = text.toCharArray();
           for (int i = 0; i < strLen; i++) {           // For Each Character in String (Except Last
//           int c = (int)text.charAt( i ) - CHAR_START;  // Calculate Character Index (Offset by First Char in Font)
               int c = -1; // text.indexOf(i);
               for (int j = 0; j < strList.size(); j++) {
                   if (single[i] == strList.get(j).charAt(0)) {
                       c = j;
                       break;
                   }
               }
//           len += ( charWidths[c] * scaleX );           // Add Scaled Character Width to Total Length
               len += (charWidthList.get(c) * scaleX);           // Add Scaled Character Width to Total Length
           }
       }
       // sxg end

      len += ( strLen > 1 ? ( ( strLen - 1 ) * spaceX ) * scaleX : 0 );  // Add Space Length
      return len;                                     // Return Total Length
   }

   //--Get Width/Height of Character--//
   // D: return the scaled width/height of a character, or max character width
   //    NOTE: since all characters are the same height, no character index is required!
   //    NOTE: excludes spacing!!
   // A: chr - the character to get width for
   // R: the requested character size (scaled)
//   public float getCharWidth(char chr)  {
//      int c = chr - CHAR_START;                       // Calculate Character Index (Offset by First Char in Font)
//      return ( charWidths[c] * scaleX );              // Return Scaled Character Width
//   }
   public float getCharWidthMax()  {
      return ( charWidthMax * scaleX );               // Return Scaled Max Character Width
   }
   public float getCharHeight() {
      return ( charHeight * scaleY );                 // Return Scaled Character Height
   }

   //--Get Font Metrics--//
   // D: return the specified (scaled) font metric
   // A: [none]
   // R: the requested font metric (scaled)
   public float getAscent()  {
      return ( fontAscent * scaleY );                 // Return Font Ascent
   }
   public float getDescent()  {
      return ( fontDescent * scaleY );                // Return Font Descent
   }
   public float getHeight()  {
      return ( fontHeight * scaleY );                 // Return Font Height (Actual)
   }

    public SpriteBatch getBatch() {
        return batch;
    }

    private static final String SD_PATH = "/sdcard/dskqxt/pic/";
    private static final String IN_PATH = "/dskqxt/pic/";

    /**
     * 随机生产文件名
     *
     * @return
     */
    private static String generateFileName() {
        return UUID.randomUUID().toString();
    }

    private int bitmapId = 0;
    /**
     * 保存bitmap到本地
     *
     * @param context
     * @param mBitmap
     * @return
     */
    public String saveBitmap(Context context, Bitmap mBitmap) {
        String filename = "pippo"+"_" + bitmapId +".png";
        File sd = Environment.getExternalStorageDirectory();
        File dest = new File(sd, filename);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(dest);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


//        return filePic.getAbsolutePath();
        return sd.getAbsolutePath();
    }

    /** 获取纹理id */
    public int getTextureId() {
        return textureId;
    }

    /**
     * 写字的起始点
     */
    public float[] getFontStartXY() {
        return new float[]{fontStartX, fontStartY};
    }

    /** 每个字的宽高 */
    public float[] getFontWidthHeight() {
        return new float[]{cellWidth, cellHeight};
    }

    /**
     * 多个字的宽高
     */
    public float[] getFontsWidthHeight(String str) {
        char strTemp;
        char[] charArray = str.toCharArray();
//        Log.i(TAG, "getFontsWidthHeight: charArray.length=" + charArray.length); // charArray.length=0
//        for (char ch : charArray) {
//            Log.i(TAG, "getFontsWidthHeight: ch=" + ch);
//        }
        /**
         * t: charArray.length=4
         06-05 13:20:41.911 9001-9548/com.grasp.zhujingju.myapplication I/Print-GLText: getFontsWidthHeight: ch=客
         06-05 13:20:41.911 9001-9548/com.grasp.zhujingju.myapplication I/Print-GLText: getFontsWidthHeight: ch=厅
         06-05 13:20:41.911 9001-9548/com.grasp.zhujingju.myapplication I/Print-GLText: getFontsWidthHeight: ch=厨
         06-05 13:20:41.911 9001-9548/com.grasp.zhujingju.myapplication I/Print-GLText: getFontsWidthHeight: ch=房
         */
        float widths = 0.f;
        for (int i = 0; i < charArray.length; i++) {
//            if(str.length() > 1) strTemp = str.substring(i, i + 1);
//            else strTemp = str.substring(i);
            strTemp = charArray[i];

            for (int j = 0; j < strList.size(); j++) {
//                if (strTemp.equals(strList.get(j)) && j < charWidthList.size()) {
                if (strTemp == strList.get(i).charAt(0) && j < charWidthList.size()) {
                    widths += charWidthList.get(j); // Invalid index 28, size is 11
                    break;
                }
            }

        }
//        Log.i(TAG, "getFontsWidthHeight: str=" + str + ", widths=" + widths + ", strList.size()=" + strList.size() + ",charWidthList.size()=" + charWidthList.size());
        return new float[]{widths, cellHeight};
    }

    /** 字的起始边距（上边距、下边距）到屏幕的投影 */
    private double[] mCameraMarginWidthHeight;

    /** 单个字的宽高到屏幕的投影 */
    private double[] mCameraFontWidthHeight;

    public void setmCameraMarginWidthHeight(double[] mCameraMarginWidthHeight) {
        this.mCameraMarginWidthHeight = mCameraMarginWidthHeight;
//        Log.i(TAG, "setmCameraMarginWidthHeight: (" + mCameraMarginWidthHeight[0] + ", " + mCameraMarginWidthHeight[1] + ")");
    }

    public void setmCameraFontWidthHeight(double[] mCameraFontWidthHeight) {
        this.mCameraFontWidthHeight = mCameraFontWidthHeight;
//        Log.i(TAG, "setmCameraFontWidthHeight: (" + mCameraFontWidthHeight[0] + ", " + mCameraFontWidthHeight[1] + ")");
    }
}
