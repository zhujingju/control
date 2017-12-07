/*     */ package com.ezvizuikit.open;
/*     */ 
/*     */ public class EZUIError
/*     */ {
/*     */   private String errorString;
/*     */   private int internalErrorCode;
/*     */   public static final String UE_ERROR_ACCESSTOKEN_ERROR_OR_EXPIRE = "UE001";
/*     */   public static final String UE_ERROR_APPKEY_ERROR = "UE002";
/*     */   public static final String UE_ERROR_CAMERA_NOT_EXIST = "UE004";
/*     */   public static final String UE_ERROR_DEVICE_NOT_EXIST = "UE005";
/*     */   public static final String UE_ERROR_PARAM_ERROR = "UE006";
/*     */   public static final String UE_ERROR_CAS_MSG_PU_NO_RESOURCE = "UE101";
/*     */   public static final String UE_ERROR_TRANSF_DEVICE_OFFLINE = "UE102";
/*     */   public static final String UE_ERROR_INNER_STREAM_TIMEOUT = "UE103";
/*     */   public static final String UE_ERROR_INNER_VERIFYCODE_ERROR = "UE104";
/*     */   public static final String UE_ERROR_PLAY_FAIL = "UE105";
/*     */   public static final String UE_ERROR_TRANSF_TERMINAL_BINDING = "UE106";
/*     */   public static final String UE_ERROR_INNER_DEVICE_NULLINFO = "UE107";
/*     */   public static final String UE_ERROR_NOT_FOUND_RECORD_FILES = "UE108";
/*     */ 
/*     */   public EZUIError(String errorString, int internalErrorCode)
/*     */   {
/*  99 */     this.errorString = errorString;
/* 100 */     this.internalErrorCode = internalErrorCode;
/*     */   }
/*     */ 
/*     */   public String getErrorString()
/*     */   {
/* 108 */     return this.errorString;
/*     */   }
/*     */ 
/*     */   public int getInternalErrorCode()
/*     */   {
/* 116 */     return this.internalErrorCode;
/*     */   }
/*     */ }

/* Location:           C:\Users\zhujingju\Desktop\EZUIKit_1.1.0.jar
 * Qualified Name:     com.ezvizuikit.open.EZUIError
 * JD-Core Version:    0.6.0
 */