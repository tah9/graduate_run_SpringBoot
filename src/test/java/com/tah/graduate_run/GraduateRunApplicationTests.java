package com.tah.graduate_run;

import com.tah.graduate_run.entity.SysUser;
import com.tah.graduate_run.service.user.SysUserInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.*;
import java.nio.channels.FileChannel;
@SpringBootTest
class GraduateRunApplicationTests {
    @Resource
    private RedisTemplate<String, String> strRedisTemplate;

    @Resource
    SysUserInfoService infoService;

    public static void main(String[] args) {

    }



    @Test
    void byThumbUpCount(){
    }
    class MyFile{

        /**
         * 遍历文件夹中文件
         * @param dicpath
         * @return
         */
        public  boolean ListFiles(String dicpath) {
            File file=new File(dicpath);

            if (!file.isDirectory()) {
                System.out.println("必须是文件夹!");
                return false;
            }

            if (!(file.exists() && file.isDirectory())) {
                System.out.println("目录不存在!");
                return false;
            }

            File[] files=file.listFiles();
            for (File file2 : files) {
                System.out.println(file2.getName());
            }
            return true;
        }
        /**
         * 删除文件(包括文件夹)
         * @param filepath
         * @return
         */
        public  boolean DeleteFile(String filepath) {
            File file=new File(filepath);
            if (file.exists()) {
                file.delete();
                return true;
            }
            else {
                return false;
            }
        }
        /**
         * 文件重命名
         * @param oldname
         * @param newname
         * @return
         */
        public  boolean Rename(String oldname,String newname) {
            File newfile=new File(newname);
            File oldfile=new File(oldname);
            if (newfile.exists()) {
                System.out.println("已经存在"+newname);
                return false;
            }
            if (!oldfile.exists()) {
                System.out.println("不存在目标文件"+oldname);
                return false;
            }
            oldfile.renameTo(newfile);
            return true;
        }
        /**
         * 复制文件,比另一个复制方式要快
         * @param oripath
         * @param despath
         * @return
         * @throws IOException
         */
        public  boolean FileCopy(String oripath,String despath) throws IOException {
            FileInputStream fileInputStream=null;
            FileOutputStream fileOutputStream=null;
            FileChannel in=null;
            FileChannel out=null;
            try
            {
                fileInputStream=new FileInputStream(oripath);
                fileOutputStream=new FileOutputStream(despath);
                in=fileInputStream.getChannel();//得到文件通道
                out=fileOutputStream.getChannel();
                in.transferTo(0, in.size(), out);//连接in与out通道,然后写入out通道
                return true;
            }
            catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            finally {
                fileInputStream.close();
                fileOutputStream.close();
                in.close();
                out.close();
            }
        }
        /**
         * 文件复制,比另一种复制方式要慢
         * @param strings
         * @throws IOException
         */
        public  void FileCopy(String[] strings) throws IOException {
            if (strings.length!=2) {
                System.out.println("请输入俩个文件路径!");
                return;
            }
            InputStream fileInputStream=null;
            FileOutputStream fileOutputStream=null;
            try {
                fileInputStream=new FileInputStream(strings[0]);
                fileOutputStream=new FileOutputStream(strings[1]);
                int len=0;
                byte[] buf=new byte[1024];
                while((len=fileInputStream.read(buf))!=-1)
                {
                    fileOutputStream.write(buf,0,len);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            finally {
                fileInputStream.close();
                fileOutputStream.close();
            }
        }
    }
}
