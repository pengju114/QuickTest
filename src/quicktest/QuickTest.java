/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package quicktest;

import com.pj.rest.RestURIParser;
import com.pj.rest.RestURIPart;
import com.pj.rest.RestURISQLBuilder;
import java.io.File;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import sun.misc.BASE64Decoder;


interface FileRecv{
        public void receive(File file);
    }
/**
 *
 * @author luzhenwen
 */
public class QuickTest implements FileRecv{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        // TODO code application logic here
//        Pattern pattern = Pattern.compile("((\\%3D)|(=))|((\\%27)|(\\')|(\\-\\-)|(\\%3B)|(:))", Pattern.CASE_INSENSITIVE);
//        String val = ":name=323'";
//        Matcher matcher = pattern.matcher(val);
//        
//        while (matcher.find()) {  
//            System.out.println(val.substring(matcher.start(), matcher.end()));
//        }
//        System.out.println(val.replaceAll(pattern.pattern(), ""));
//        String name = URLDecoder.decode("/web-framework/client.service/name.eq(\"jjj\")&age.!lt(23)|gg.gt(66)", "utf-8");
//        System.out.println(name);
//        deepInterateFiles("/Users/luzhenwen/Documents/e_mlfs", new QuickTest());
//  "/userNamePart/get/10/role/list/roleId.eq(userRoleId)&name.!eq(123.00)&age.gt(32)|des.contains(\"Jone\")/authority/list/authorityId.in(roleAuthorityId)?namr=5678&hhh=ooo"
        
        RestURISQLBuilder.RestURIBuilderDataSource dataSource = new RestURISQLBuilder.RestURIBuilderDataSource() {
            public String evaluatePath(RestURISQLBuilder restURISQLBuilder, String path) {
                return null;
            }

            public String evaluateVarName(RestURISQLBuilder restURISQLBuilder, String path) {
                return null;
            }

            public Map<String, Object> getKeysAndObjects(RestURISQLBuilder restURISQLBuilder, RestURIPart part) {
                HashMap<String,Object> map = new HashMap<String, Object>(3);
                map.put("id", 123);
                map.put("atc", "tycit");
                map.put("iou", "哈哈");
                
                return map;
            }
        };

        long start = System.currentTimeMillis();
        List<RestURIPart> parts = RestURIParser.parse("/user/list/userId.eq(12)/role/list/userId.eq(userId)",null);
        RestURISQLBuilder builder = new RestURISQLBuilder(RestURISQLBuilder.Database.MySQL);
        builder.addDataSource(dataSource);
        builder.append(parts);
        RestURISQLBuilder.Tuple<CharSequence> result = builder.build();
        long end = System.currentTimeMillis();
        System.out.println(parts);
        System.out.println(result.keyObject);
        System.out.println(result.valueObject);
        System.out.println(end - start);
//        
//        Pattern p = Pattern.compile("^(\\w+)(\\.(!)?([a-z]+)\\(([^\\(\\)]+)\\))?$");
//        Pattern ao = Pattern.compile("^[^&\\|]+([&\\|][^&\\|]+)*$");
//        
//        Matcher m = p.matcher(input);
//        
//        Matcher am = ao.matcher(input);
//        if (am.find()) {
//            for (int i = 0; i <= am.groupCount(); i++) {
//                System.out.println(i+":"+am.group(i));
//            }
//        }
//        
//        if (m.find()) {
//            for (int i = 0; i <= m.groupCount(); i++) {
//                System.out.println(i+":"+m.group(i));
//            }
//        }
//        
    }
    
    
    public static void deepInterateFiles(String path,FileRecv recv){
        File file = new File(path);
        if (file.isDirectory()) {
            File[] fs = file.listFiles();
            if (fs != null) {
                for (File f : fs) {
                    if (f.isDirectory()) {
                        deepInterateFiles(f.getAbsolutePath(),recv);
                    }else{
                        recv.receive(f);
                    }
                }
            }
        }else{
            recv.receive(file);
        }
    }

    @Override
    public void receive(File file) {
        
        if (file.getAbsolutePath().matches(".*\\.\\w+$")) {
            return;
        }
        
//        System.out.println(file.getAbsolutePath());
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            String name = new String(decoder.decodeBuffer(file.getName()),"UTF-8");
            if (!name.matches(".*\\.\\w+$")) {
                name += ".RMVB";
            }
            
            File newFile = new File(file.getParentFile().getAbsolutePath(), name);
            
            if (file.renameTo(newFile)) {
                System.out.println(file.getAbsolutePath()+ " -> " + newFile.getName());
            }
        } catch (Exception e) {
        }
        
    }
}


