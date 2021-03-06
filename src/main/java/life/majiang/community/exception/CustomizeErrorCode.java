package life.majiang.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {
    QUESTION_NOT_FOUND(2001, "你找的问题不在了,换一个试试"),
    TARGET_PARAM_NOT_FOUND(2002, "为选中任何问题或评论进行回复"),
    NO_LONG(2003, "需要登录，请登录在重试"),
    SYS_ERROR(2004, "服务器异常了，在等等..."),
    TYPE_PARAM_WRONG(2005, "评论类型错误或不存在..."),
    COMMENT_NOT_FOUND(2006, "回复的评论不在了,换一个试试..."),
    COMMENT_IS_EMPTY(2007, "输入内容不能为空..."),
    READ_NOTIFICATION_FAIL(2008, "读别人的信息了？？？？..."),
    NOTIFICATION_NOT_FOUND(2008, "消息不翼而飞？？？？..."),
    FILE_UPLOAD_FAIL(2009, "图片上传失败..."),
    ;
    private Integer code;
    private String message;

    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    CustomizeErrorCode(String message) {
        this.message = message;
    }

    CustomizeErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
