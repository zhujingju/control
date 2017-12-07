package com.ezvizuikit.open;

import android.view.View;
import java.util.Calendar;
import java.util.List;

abstract interface EZUIPlayerInterface
{
  public abstract void setCallBack(EZUIPlayer.EZUIPlayerCallBack paramEZUIPlayerCallBack);

  public abstract void setUrl(String paramString);

  public abstract int getStatus();

  public abstract void startPlay();

  public abstract void seekPlayback(Calendar paramCalendar);

  public abstract Calendar getOSDTime();

  public abstract void stopPlay();

  public abstract void pausePlay();

  public abstract void resumePlay();

  public abstract void releasePlayer();

  public abstract List getPlayList();

  public abstract void setSurfaceSize(int paramInt1, int paramInt2);

  public abstract void setLoadingView(View paramView);
}

/* Location:           C:\Users\zhujingju\Desktop\EZUIKit_1.1.0.jar
 * Qualified Name:     com.ezvizuikit.open.EZUIPlayerInterface
 * JD-Core Version:    0.6.0
 */