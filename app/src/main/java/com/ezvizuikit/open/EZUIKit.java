/*    */ package com.ezvizuikit.open;
/*    */ 
/*    */ import android.app.Application;
/*    */ import android.text.TextUtils;
/*    */ import com.videogo.openapi.EZOpenSDK;
/*    */ import com.videogo.openapi.EzvizAPI;
/*    */ import com.videogo.util.LogUtil;
/*    */ 
/*    */ public class EZUIKit
/*    */ {
/*    */   public static final String EZUIKit_Version = "1.1.0";
/*    */ 
/*    */   public static void initWithAppKey(Application application, String appkey)
/*    */   {
/* 27 */     if (TextUtils.isEmpty(appkey)) {
/* 28 */       LogUtil.d("EZUIKit", "appkey is null");
/* 29 */       return;
/*    */     }
/* 31 */     EZOpenSDK.initLib(application, appkey, "");
/* 32 */     EzvizAPI.getInstance().setExterVer("Ez.1.1.0");
/*    */   }
/*    */ 
/*    */   public static void setDebug(boolean debug)
/*    */   {
/* 41 */     EZOpenSDK.showSDKLog(debug);
/*    */   }
/*    */ 
/*    */   public static void setAccessToken(String accessToken)
/*    */   {
/* 49 */     if (TextUtils.isEmpty(accessToken)) {
/* 50 */       LogUtil.d("EZUIKit", "accessToken is null");
/* 51 */       return;
/*    */     }
/* 53 */     if (EZOpenSDK.getInstance() == null) {
/* 54 */       LogUtil.d("EZUIKit", "OpenSDK is not init");
/*    */     }
/* 56 */     EZOpenSDK.getInstance().setAccessToken(accessToken);
/*    */   }
/*    */ }

/* Location:           C:\Users\zhujingju\Desktop\EZUIKit_1.1.0.jar
 * Qualified Name:     com.ezvizuikit.open.EZUIKit
 * JD-Core Version:    0.6.0
 */