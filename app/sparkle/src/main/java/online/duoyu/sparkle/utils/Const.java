package online.duoyu.sparkle.utils;

/**
 * Created by littlekey on 12/19/16.
 */

public class Const {
  public static final String LAST_ACTION = "last_action";
  public static final String LAST_COOKIE = "last_cookie";
  public static final String LAST_SESSION_ID = "last_session_id";
  public static final String LAST_USER_ID = "last_user_id";
  public static final String LAST_PASS_HASH = "last_pass_hash";
  public static final String EMPTY_STRING = "";
  public static final String USER_AGENT = "User-Agent";
  public static final String USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.103 Safari/537.36";
  public static final int IMAGE_ITEM_COUNT_PER_PAGE = 20;
  /***********************************
   * Action
   **********************************/
  public static final Integer ACTION_MAIN = 0;
  public static final Integer ACTION_SHOW_HIDE = 1;
  public static final Integer ACTION_LIKED = 2;
  /************************************
   * Key
   ***********************************/
  public static final String KEY_EXTRA = "extra";
  public static final String KEY_API_TYPE = "api_url";
  public static final String KEY_API_QUERY = "api_query";
  public static final String KEY_API_PATH = "api_path";
  public static final String KEY_ID = "id";
  public static final String KEY_MODEL = "model";
  public static final String KEY_NEW_PASSWORD = "new_password";
  public static final String KEY_OLD_PASSWORD = "old_password";
  public static final String KEY_MOBILE_PN = "mobile_pn";
  public static final String KEY_VERIFICATION_CODE = "verification_code";
  public static final String KEY_USER_NAME = "UserName";
  public static final String KEY_PASSWORD = "PassWord";
  public static final String KEY_PASSWORD_CHECK = "PassWord_Check";
  public static final String KEY_QUERY = "query";
  public static final String KEY_IDENTITY = "identity";
  public static final String KEY_AVATAR = "avatar";
  public static final String KEY_GENDER = "gender";
  public static final String KEY_REGION = "region";
  public static final String KEY_USER_ID = "user_id";
  public static final String KEY_TYPE = "type";
  public static final String KEY_GROUP_COUNT = "group_count";
  public static final String KEY_DISPLAY_NAME = "display_name";
  public static final String KEY_MEMBERS_DISPLAY_NAME = "members_display_name";
  public static final String KEY_BIO = "bio";
  public static final String KEY_HEIGHT = "height";
  public static final String KEY_COOKIE = "cookie";
  public static final String KEY_COOKIE_DATE = "CookieDate";
  public static final String KEY_SET_COOKIE = "Set-Cookie";
  public static final String KEY_PAGE = "page";
  public static final String KEY_P = "p";
  public static final String KEY_T = "t";
  public static final String KEY_S = "s";
  public static final String KEY_LV = "lv";
  public static final String KEY_IGNEOUS = "igneous";
  public static final String KEY_ACT = "act";
  public static final String KEY_CODE = "CODE";
  public static final String KEY_ENABLE_SWIPE_REFRESH = "enable_swipe_refresh";
  public static final String KEY_PATH = "key_path";
  public static final String KEY_GID = "gid";
  public static final String KEY_FAV_NOTE = "favnote";
  public static final String KEY_UPDATE = "update";
  public static final String KEY_FAV_CAT = "favcat";
  public static final String KEY_APPLY = "apply";
  public static final String KEY_F_APPLY = "f_apply";
  public static final String KEY_TOKEN = "token";
  public static final String KEY_F_SEARCH = "f_search";
  public static final String KEY_HC = "hc";
  public static final String KEY_POSITION = " position";
  public static final String KEY_TOKEN_LIST = "token_list";
  public static final String KEY_AGREE_TO_TERMS = "agree_to_terms";
  public static final String KEY_RC = "rc";
  public static final String KEY_TEMPORARY_HTTPS = "temporary_https";
  public static final String KEY_TERMSREAD = "termsread";
  public static final String KEY_COPPA_USER = "coppa_user";
  public static final String KEY_EMAIL_ADDRESS = "EmailAddress";
  public static final String KEY_EMAIL_ADDRESS_TWO = "EmailAddress_two";
  public static final String KEY_TIME_OFFSET = "time_offset";
  public static final String KEY_TIME_STAMP = "timestamp";
  public static final String KEY_REG_ID = "regid";
  public static final String KEY_REG_CODE = "reg_code";
  public static final String KEY_DO = "do";
  public static final String KEY_NAME = "name";
  public static final String KEY__ = "_";
  public static final String KEY_HAS_MORE = "has_more";
  public static final String KEY_ADV_SEARCH = "advsearch";
  /***********************************
   * EXTRA
   **********************************/
  public static final String EXTRA_TASK_ID = KEY_EXTRA + "task_id";
  public static final String EXTRA_IDENTITY = KEY_EXTRA + "identity";
  public static final String EXTRA_POSITION = KEY_EXTRA + "position";
  public static final String EXTRA_UPLOAD_IMAGE = KEY_EXTRA + "upload_image";
  public static final String EXTRA_SELECT_IMAGE = KEY_EXTRA + "select_image";
  public static final String EXTRA_IMAGE_TYPE = KEY_EXTRA + "image_type";
  public static final String EXTRA_SOURCE_IMAGE_URI = KEY_EXTRA + "source_uri";
  public static final String EXTRA_DEST_IMAGE_URI = KEY_EXTRA + "dest_uri";
  public static final String EXTRA_BORDER_TYPE = KEY_EXTRA + "draw_circle_border";
  public static final String EXTRA_ROOM_IDENTITY = KEY_EXTRA + "room_number";
  public static final String EXTRA_MODEL = KEY_EXTRA + "model";
  public static final String EXTRA_MODEL_LIST = KEY_EXTRA + "model_list";
  public static final String EXTRA_IDENTITY_LIST = KEY_EXTRA + "identity_list";
  public static final String EXTRA_URL = KEY_EXTRA + "url";
  public static final String EXTRA_PAGES = KEY_EXTRA + "pages";
  public static final String EXTRA_TOKEN = KEY_EXTRA + "token";
  public static final String EXTRA_PROGRESS = KEY_EXTRA + "progress";
  /***********************************
   * API
   ***********************************/
  private static final String API_EX_HOST_URL = "http://exhentai.org";
  // earth
  public static final String API_ROOT = API_EX_HOST_URL + "/";
  public static final String API_HOME_LIST = API_EX_HOST_URL + "/";
  /********************************
   * Network Related
   ********************************/
  public static final String NETWORK_HEADER_SSID = "SSID";
  /***********************************
   * UM Social
   ***********************************/
  public static final String PLATFORM_QQ = "qq";
  public static final String PLATFORM_WEIXIN = "wechat";
  public static final String PLATFORM_WEIBO = "weibo";
  public static final String THIRD_ACCOUNT = "third_account";
  public static final String THIRD_ACCOUNT_QQ = "account_qq";
  public static final String THIRD_ACCOUNT_SINA = "account_sina";
  public static final String THIRD_ACCOUNT_WX = "account_wx";
  public static final String DESCRIPTOR = "com.umeng.share";
  public static final String SINA_REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
  public static final String APP_ID_SINA = "1490658744";
  public static final String APP_SECRET_SINA = "29c98275a96b8695c55f8b125aef8519";
  public static final String APP_ID_QQ = "1105346526";
  public static final String APP_KEY_QQ = "x9u8wC3NllIEZSDY";
  public static final String APP_ID_WX = "wx4b50e46e23ee3dd4";
  public static final String APP_SECRET_WX = "7fbeb429ea9920a0300f7a34e791bbed";
  public static final String TARGET_URL = "https://yuanqi.tv";
  /************************************
   * ConstValues
   ***********************************/
  public static final String NAME = "name";
  public static final String TAGS = "tags";
  public static final String ON = "on";
  public static final String ZERO = "0";
  public static final String ONE = "1";
  public static final String EIGHT = "8";
  public static final String LOGIN = "Login";
  public static final String REG = "Reg";
  public static final String NOT_FOUND = "notfound";
  public static final String PAGE_NUMBER = "page number";
  public static final String COMMENT = "comment";
  public static final String IMAGE = "image";
  public static final String ALL = "all";
  public static final String TAG = "tag";
  public static final String SAVE_DATA = "SaveData";
  public static final int MIN_USERNAME_LENGTH = 3;
  public static final int MIN_DISPLAY_NAME_LENGTH = 3;
  public static final int REGISTER_CODE_LENGTH = 6;
  public static final int MIN_PASSWORD_LENGTH = 8;
  public static final int MAX_PASSWORD_LENGTH = 30;
  public static final int MIN_NICKNAME_LENGTH = 1;
  public static final int MAX_NICKNAME_LENGTH = 12;
  public static final int MAX_SIGNATURE_LENGTH = 30;
  public static final int MIN_SIGNATURE_LENGTH = 0;
  public static final int PHONE_NUM_LENGTH = 11;
  public static final int MAX_LIVE_TITLE_LENGTH = 30;
  public static final int MIN_LIVE_TITLE_LENGTH = 2;
  public static final int VITALITY_PICK_IMAGE = 2333;
  public static final int VITALITY_CROP_IMAGE = 6666;
  public static final int VITALITY_CAPTURE = 9494;
  public static final int IMAGE_TYPE_AVATAR = 0;
  public static final int IMAGE_TYPE_COVER = 1;
  public static final float ALPHA_FULL = 1.0f;
  public static final float ALPHA_LITTLE = 0.7f;
  public static final int CLOSE_TYPE_DISBAND = 1;
  public static final int CLOSE_TYPE_EXIT = 2;
  public static final int MAX_STATURE = 200;
  public static final int MIN_STATURE = 155;
  public static final String GENDER_FEMALE = "女";
  public static final String STRING_SPACE = " ";
  /************************************
   * REGISTERWay
   ***********************************/
  public static final String REGISTER_WAY = "register_way";
  public static final int REGISTER_PHONE = 0;
  public static final int REGISTER_THIRD_ACCOUNT = 1;


  private Const() {
  }
}