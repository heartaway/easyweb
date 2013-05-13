package com.taobao.easyweb.hsf.transfomer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Json parser
 *
 */
@SuppressWarnings({"unchecked"})
public class JsonParser {
    private static final Log logger = LogFactory.getLog(JsonParser.class);
    private static final String lineSep = System.getProperty("line.separator", "\n");
    private static Object OBJ_NULL = new Object();
    //private StringBuilder typeStructure = new StringBuilder();
    private StringBuilder sb;
    private int sbi;
    private int tokenIndex1 = -1;
    private int tokenIndex2 = -1;
    // private boolean escaped = false;
    private boolean isWaitStringEnd = false;

    /**
     * Stack 中只可能存在5种符号： [ 对应object为List { 对应object为Map " 对应Object为String '
     * 对应Object为String : 对应Object为Map的Key
     */
    private char[] stack;
    private Object[] objs;
    private int stacki = -1;
    private int initialCapacity = 512;
    private int increaseSize = 32;

    private Object tempObj;


    private void increaseStack() {
        char[] old = this.stack;
        this.stack = new char[this.stack.length + this.increaseSize];
        System.arraycopy(old, 0, this.stack, 0, old.length);
    }


    private void push(char c, Object obj) {
        this.stacki++;
        if (this.stacki == this.stack.length) {
            increaseStack();
        }
        this.stack[this.stacki] = c;
        this.objs[this.stacki] = obj;
    }


    private char pop() {
        char c = this.stack[this.stacki];
        this.tempObj = this.objs[this.stacki];
        this.stack[this.stacki] = 0;
        this.objs[this.stacki] = null;
        this.stacki--;
        return c;
    }


    public JsonParser(String str) {
        this(new StringBuilder(str));
    }


    public JsonParser(StringBuilder sb) {
        this.sb = sb;
        this.stack = new char[this.initialCapacity];
        this.objs = new Object[this.initialCapacity];
    }


    public JsonParser(StringBuilder sb, int initialCapacity, int increaseSize) {
        this.sb = sb;
        this.initialCapacity = initialCapacity;
        this.increaseSize = increaseSize;
        this.stack = new char[this.initialCapacity];
        this.objs = new Object[this.initialCapacity];
    }


    public void reset() {
        Arrays.fill(this.stack, (char) 0);
        this.stacki = -1;
        this.tokenIndex1 = -1;
        this.tokenIndex2 = -1;
        // this.escaped = false;
        this.sbi = 0;
    }


    private static boolean isWhiteSpace(char c) {
        return c == ' ' || c == ' ' || c == '\r' || c == '\n';
    }


    private Object completeNoneQuotedToken(StringBuilder sb) {
        if (this.tokenIndex1 != -1 && this.tokenIndex2 != -1) {
            String str = sb.substring(this.tokenIndex1, this.tokenIndex2);
            this.tokenIndex1 = -1;
            this.tokenIndex2 = -1;
            if ("null".equals(str))
                return OBJ_NULL;
            if ("false".equals(str)) {
                return Long.valueOf(0);
            }
            else if ("true".equals(str)) {
                return Long.valueOf(1);
            }
            else if ("".equals(str)) {
                return null;
            }
            return Long.valueOf(str);
        }
        return null;
    }


    /**
     * block包含： 1：[] 2:{} 3:"xxx" 4:'xxx' 5: 1979 pop 之后调用
     * 
     * @return 不为null， 表示解析结束，返回解析结果
     */
    private Object blockCompleted(Object obj) {
        if (this.stacki == -1)
            return obj;
        char c = this.stack[this.stacki];
        switch (c) {
        case ':':
            // 说明这个block是作为map的value的。
            /*
             * this.pop(); Object key = this.pickTempObj(); Map map = (Map)
             * this.objs[this.stacki]; map.put(key, obj); break;
             */
        case '[':
        case '{':
            // 不处理，等待后面的','或']/}'处理
            break;
        case '"':
        case '\'':
            throw new IllegalArgumentException("A block is completed, but '" + c + "' in current stack");
        default:
            throw new IllegalArgumentException(c + " in stack");
        }
        this.tokenIndex1 = -1;
        this.tokenIndex2 = -1;
        return null;
    }


    private StringBuilder parseCodingString(StringBuilder codingStr) {
        int escapeIndex;
        while ((escapeIndex = codingStr.indexOf("\\")) != -1) {
            char c = codingStr.charAt(escapeIndex + 1);
            /*if (c == '"' || c == '\'') {
             codingStr.deleteCharAt(escapeIndex);
             }
             else */
            if (c == 'u') {
                String unicode = codingStr.substring(escapeIndex + 2, escapeIndex + 6);
                char unicodeChar;
                try {
                    unicodeChar = (char) Integer.parseInt(unicode, 16);
                }
                catch (Exception e) {
                    throw new IllegalArgumentException("Invalid unicode", e);
                }
                codingStr.replace(escapeIndex, escapeIndex + 6, String.valueOf(unicodeChar));
            }
            else if (c == 'b') {
                codingStr.replace(escapeIndex, escapeIndex + 2, String.valueOf('\b'));
            }
            else if (c == 't') {
                codingStr.replace(escapeIndex, escapeIndex + 2, String.valueOf('\t'));
            }
            else if (c == 'f') {
                codingStr.replace(escapeIndex, escapeIndex + 2, String.valueOf('\f'));
            }
            else if (c == 'r') {
                codingStr.replace(escapeIndex, escapeIndex + 2, String.valueOf('\r'));
            }
            else if (c == 'n') {
                codingStr.replace(escapeIndex, escapeIndex + 2, String.valueOf('\b'));
            }
            else if (c == '"') {
                codingStr.replace(escapeIndex, escapeIndex + 2, String.valueOf('\"'));
            }
            else if (c == '\'') {
                codingStr.replace(escapeIndex, escapeIndex + 2, String.valueOf('\''));
            }
            else if (c == '/') {
                codingStr.replace(escapeIndex, escapeIndex + 2, String.valueOf('/'));
            }
            else {
                //http:\/\/pic.kaixin001.com\/logo\/64\/31\/50_8643173_1.jpg
                throw new IllegalArgumentException("Invalid escape sequence:" + codingStr);
            }
        }
        //String p = "fffffff\]u5555]ff";
        return codingStr;
    }


    private static Object getRemainObj(Object remainObj) {
        return remainObj != OBJ_NULL ? remainObj : null;
    }


    @SuppressWarnings("rawtypes")
	public Object parse() {
        logger.info("Block to be parsed:" + this.sb);
        Object res;
        for (int n = sb.length(); sbi < n; sbi++) {
            Object remainObj;
            char c = sb.charAt(sbi);
            switch (c) {
            case '[':
                if (isWaitStringEnd) {
                    this.tokenIndex2++;
                }
                else {
                    this.push('[', new ArrayList());
                    //this.typeStructure.append("List<");
                }
                this.tokenIndex1 = sbi + 1;
                this.tokenIndex2 = sbi + 1;
                break;
            case '{':
                if (isWaitStringEnd) {
                    this.tokenIndex2++;
                }
                else {
                    this.push('{', new LinkedHashMap());
                    //this.typeStructure.append("Map<");
                }
                this.tokenIndex1 = sbi + 1;
                this.tokenIndex2 = sbi + 1;
                break;
            case ']': {
                if (isWaitStringEnd) {
                    this.tokenIndex2++;
                    break;
                }
                remainObj = this.tempObj != null ? this.tempObj : this.completeNoneQuotedToken(sb);
                if ('[' != this.pop())
                    throw new IllegalArgumentException("no '[' paired with ']'");
                List list = (List) this.tempObj;
                if (remainObj != null) {
                    list.add(getRemainObj(remainObj));
                }
                //this.typeStructure.append(">");
                res = blockCompleted(list);
                if (res != null)
                    return res;
                break;
            }
            case '}': {
                if (isWaitStringEnd) {
                    this.tokenIndex2++;
                    break;
                }
                remainObj = this.tempObj != null ? this.tempObj : this.completeNoneQuotedToken(sb);
                Map map;
                if (remainObj != null) {
                    if (':' != this.pop()) {
                        throw new IllegalArgumentException("no ':' paired with remainObj when '}' ocures");
                    }
                    Object key = this.tempObj;
                    if ('{' != this.pop()) {
                        throw new IllegalArgumentException("no '{' paired with '}'");
                    }
                    map = (Map) this.tempObj;
                    map.put(key, getRemainObj(remainObj));
                }
                else {
                    if ('{' != this.pop()) {
                        throw new IllegalArgumentException("no '{' paired with '}'");
                    }
                    map = (Map) this.tempObj;
                }
                //this.typeStructure.append(">");
                res = this.blockCompleted(map);
                if (res != null)
                    return res;
                break;
            }
            case ':':
                if (isWaitStringEnd) {
                    this.tokenIndex2++;
                    break;
                }
                remainObj = this.tempObj != null ? this.tempObj : this.completeNoneQuotedToken(sb);
                if (this.stack[this.stacki] != '{') {
                    throw new IllegalArgumentException("':' occurs but last separator is not '{'");
                }
                if (remainObj != null) {
                    this.push(c, getRemainObj(remainObj));
                }
                else {
                    throw new IllegalArgumentException("':' occurs but no lastObj or token");
                }
                this.tempObj = null;
                this.tokenIndex1 = sbi + 1;
                this.tokenIndex2 = sbi + 1;
                break;
            case '\'':
            case '"':
                /*
                 * if (escaped) { tokenIndex2++; escaped = false; } else
                 */
                if (this.stack[this.stacki] == c) {
                    // 一个字符串完成
                    String str = sb.substring(this.tokenIndex1, this.tokenIndex2);
                    this.tokenIndex1 = -1;
                    this.tokenIndex2 = -1;
                    this.isWaitStringEnd = false;
                    this.pop();
                    this.tempObj = parseCodingString(new StringBuilder(str)).toString();
                    res = this.blockCompleted(str);
                    if (res != null)
                        return res;
                    break;
                }
                else if (this.stack[this.stacki] == '"' || this.stack[this.stacki] == '\'') {
                    // 被包含在单/双引号中
                    this.tokenIndex2++;
                }
                else {
                    // 一个字符串开始
                    this.push(c, "StringEndDesired");
                    this.tokenIndex1 = sbi + 1;
                    this.tokenIndex2 = sbi + 1;
                    this.isWaitStringEnd = true;
                }
                break;
            case ',':
                if (isWaitStringEnd) {
                    this.tokenIndex2++;
                    break;
                }
                remainObj = this.tempObj != null ? this.tempObj : this.completeNoneQuotedToken(sb);
                if (remainObj == null) {
                    throw new IllegalArgumentException("',' occurs but no lastObj or token");
                }
                if (this.stack[this.stacki] == '[') {
                    List list = (List) this.objs[this.stacki];
                    list.add(getRemainObj(remainObj));
                }
                else if (this.stack[this.stacki] == ':') {
                    this.pop();
                    Object key = this.tempObj;
                    Map map = (Map) this.objs[this.stacki];
                    map.put(key, getRemainObj(remainObj));
                }
                else {
                    throw new IllegalArgumentException("',' occurs but last separator is not '[' or ':'");
                }
                this.tempObj = null;
                this.tokenIndex1 = sbi + 1;
                this.tokenIndex2 = sbi + 1;
                break;
            case '\\':
                if (this.isWaitStringEnd) {
                    if (sbi < n - 1 && (sb.charAt(sbi + 1) == '"' || sb.charAt(sbi + 1) == '"')) {
                        sbi++;
                        this.tokenIndex2 += 2;
                    }
                    else {
                        this.tokenIndex2++;
                    }
                    // escaped = true;
                    // this.escapePositions.add(sbi);
                    // this.tokenIndex2++;
                }
                else {
                    throw new IllegalArgumentException("Escape charactor can only be used in a string");
                }
                break;
            default:
                if (this.tokenIndex1 != -1 && this.tokenIndex2 != -1) {
                    this.tokenIndex2++;
                }
                else if (isWhiteSpace(c))
                    continue;
                else
                    throw new IllegalArgumentException("Invalid occurrence:" + c);
            }

        }
        return null; //"Not a prototype expression";
    }


    public StringBuilder reportStatus() {
        return new StringBuilder("PrototypeParser{").append("i:").append(this.sbi).append(",char:").append(
            this.sb.charAt(this.sbi)).append(",stacki:").append(this.stacki).append(",stack:").append(
            this.reportStack()).append(",tokenIndex1:").append(this.tokenIndex1).append(",tokenInde2:").append(
            this.tokenIndex2).append(",tempObj:").append(this.tempObj).append(",isWaitStringEnd:").append(
            this.isWaitStringEnd).append("}");
    }


    private String reportStack() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < this.stack.length; i++) {
            char c = this.stack[i];
            if (c == 0)
                break;
            sb.append(c).append("=").append(this.objs[i]).append(" | ");
        }
        sb.append("]");
        return sb.toString();
    }


    private static StringBuilder readFileAsString(File f) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(f));
        try {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                sb.append(line);
            return sb;
        }
        finally {
            reader.close();
        }
    }


    private static String getDeepIndent(int deep, String indent) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < deep; i++) {
            res.append(indent);
        }
        return res.toString();
    }


    @SuppressWarnings("rawtypes")
	public static void showConstructrue(Object obj, StringBuilder sb, String indent, int deep) {
        String thisIndent = getDeepIndent(deep, indent);
        if (obj instanceof Map) {
            sb.append("{").append(lineSep).append(thisIndent);
            Set<Map.Entry> entrySet = ((Map) obj).entrySet();
            for (Map.Entry e : entrySet) {
                sb.append(indent);
                showConstructrue(e.getKey(), sb, indent, deep + 1);
                sb.append(":");
                showConstructrue(e.getValue(), sb, indent, deep + 1);
                sb.append(",").append(lineSep).append(thisIndent);
            }
            sb.append("}");
        }
        else if (obj instanceof List) {
            sb.append("[").append(lineSep).append(thisIndent);
            for (Object e : (List) obj) {
                sb.append(indent);
                showConstructrue(e, sb, indent, deep + 1);
                sb.append(",").append(lineSep).append(thisIndent);
            }
            sb.append("]");
        }
        else {
            if (obj != null && !obj.getClass().equals(String.class))
                sb.append(obj).append("(").append(obj.getClass().getSimpleName()).append(")");
            else
                sb.append(obj);
        }
        // return sb.toString();
    }


    @SuppressWarnings("rawtypes")
	public static void toJsonStr(Object obj, StringBuilder sb, String indent, int deep, String quote) {
        String thisIndent = getDeepIndent(deep, indent);
        if (obj instanceof Map) {
            sb.append("{").append(lineSep).append(thisIndent);
            Set<Map.Entry> entrySet = ((Map) obj).entrySet();
            for (Map.Entry e : entrySet) {
                sb.append(indent);
                toJsonStr(e.getKey(), sb, indent, deep + 1, quote);
                sb.append(":");
                toJsonStr(e.getValue(), sb, indent, deep + 1, quote);
                sb.append(",").append(lineSep).append(thisIndent);
            }
            sb.append("}");
        }
        else if (obj instanceof List) {
            sb.append("[").append(lineSep).append(thisIndent);
            for (Object e : (List) obj) {
                sb.append(indent);
                toJsonStr(e, sb, indent, deep + 1, quote);
                sb.append(",").append(lineSep).append(thisIndent);
            }
            sb.append("]");
        }
        else {
            if (obj != null && obj.getClass().equals(String.class))
                sb.append(quote).append(obj).append(quote);
            else
                sb.append(obj);
        }
        // return sb.toString();
    }


    public static void main(String[] args) throws IOException {
        //System.out.println((int) '\u0000');
        //System.out.println((char) 0 == '\u0000');
        //System.out.println("\u4faf\u5148\u5cf0");
        //File dataFile = new File("E:/passion/workspace/kaixin001/v_userdata.txt");
        //File dataFile = new File("E:/passion/workspace/kaixin001/v_frienddata.txt");
        //File dataFile = new File("E:/passion/workspace/kaixin001/friendParkRsp.txt");
        File dataFile = new File("D:/06_passion/workspace/kaixin001/friendParkRsp.txt");
        StringBuilder data = readFileAsString(dataFile);
        JsonParser parser = new JsonParser(data);
        try {
            Object res = parser.parse();
            StringBuilder constructrue = new StringBuilder();
            showConstructrue(res, constructrue, "  ", 0);
            System.out.println("The constructure with java type(default to String) is:");
            System.out.println(constructrue);
            System.out.println("The constructure with java type(default to String) end.");

            //ParkUserData parkUserData = ParkUserData.valueOf((Map<String,Object>)res);
            //System.out.println(parkUserData);
            //List<ParkFriend> parkFriends = ParkFriend.valueOf((List<Map<String,Object>>)res);
            //System.out.println(parkFriends);
        }
        finally {
            System.out.println(parser.reportStatus());
        }
        // char c;
        // System.out.println(stack[0]);
        // System.out.println(stack[5] == '\u0000');
        // System.out.println((int) stack[6] == 0);
    }

}
