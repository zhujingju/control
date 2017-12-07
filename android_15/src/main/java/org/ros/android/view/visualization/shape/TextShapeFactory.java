package org.ros.android.view.visualization.shape;

import android.content.Context;
import android.graphics.Typeface;

import org.ros.android.view.visualization.VisualizationView;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import uk.co.blogspot.fractiousg.texample.GLText;

public class TextShapeFactory {

  private final GLText glText;

//  public TextShapeFactory(final VisualizationView view, final GL10 gl) {
//    glText = new GLText(gl, view.getContext().getAssets());
//  }
  public TextShapeFactory(final VisualizationView view, final GL10 gl, Context context, final Typeface typeface, final int size, final int padX, final int padY, double zoom, List<String> strListParam) {
    glText = new GLText(gl, view.getContext().getAssets(), context, typeface, size, padX, padY, zoom, strListParam);
  }

//  public boolean loadFont(Context context, final Typeface typeface, final int size, final int padX, final int padY, List<String> strListRender) {
//    return glText.load(context, typeface, size, padX, padY, strListRender);
//  }

//  public void loadFont(final String file, final int size, final int padX, final int padY, List<String> strListRender) {
//    glText.load(file, size, padX, padY, strListRender);
//  }

  public TextShape newTextShape(final String text, double zoom) {
    return new TextShape(glText, text, zoom);
  }

//  public boolean addFontModule(String name) {
//    return glText.addFontModule(name);
//  }

//  public List<String> getNewStrList(String name) {
//    return glText.getNewStringList(name);
//  }

//  public void addNewStrList(List<String> strNewList) {
//    glText.addNewListToOrignal(strNewList);
//  }

  public GLText getGlText() {
    return glText;
  }
}
