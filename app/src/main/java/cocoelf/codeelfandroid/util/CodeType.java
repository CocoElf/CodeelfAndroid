package cocoelf.codeelfandroid.util;

/**
 * 任务计时类型
 */

public enum  CodeType {
    CODE("编码"), DEBUG("调试"), TEST("测试"), APP("程序");

    public String name;
    CodeType(String name){
        this.name=name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    /**
     * 字符串转枚举类型
     */
    public static CodeType stringToCodeType(String string) {
        for (CodeType codeType : CodeType.values()) {
            if (string.equals(codeType.name)) {
                return codeType;
            }
        }
        return null;
    }
}

