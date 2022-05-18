import java.io.*;
import java.net.URL;
import java.util.*;
import java.text.SimpleDateFormat;

public class M3U8Download {

    public static final String PATH_SEPARATOR = File.separator;
    private static final String date = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
    public static void main(String[] args) {

        try {
            String userDir = System.getProperty("user.dir");
            String url = "https://tspc.vvvdj.com/c1/2021/12/224514-b9caed/224514.m3u8?upt=a88e0ee91643471999&news";
            File downloadList = new File(userDir + PATH_SEPARATOR + "logs"+PATH_SEPARATOR+"downloadList_"+date+".log");
            //System.out.println("userDir = " + userDir);
            if (!downloadList.exists()) {
                downloadList.createNewFile();				
                System.out.println("请把链接填写在"+downloadList+"文件");
                System.out.println("例如："+url);
            } else {
                if (downloadList.length() > 0) {
                    BufferedReader br = new BufferedReader(new FileReader(downloadList));
                    String str = null;
                    StringBuilder sb = new StringBuilder();
                    while ((str = br.readLine()) != null) {
                        if (str.startsWith("https://")) {
                            String finalStr = str;
                            new Thread(() -> {
                                new M3U8Download().dowloadM3u8(finalStr, userDir);
                            }).start();
                            sb.append("Done "+str);
                            sb.append(System.getProperty("line.separator"));
                        }else{
                            sb.append(str);
                            sb.append(System.getProperty("line.separator"));							
						}
                    }
                    br.close();

                    BufferedWriter bw = new BufferedWriter(new FileWriter(downloadList));
                    bw.write(sb.toString());
                    bw.close();
                }else {
                    System.out.println("请把链接填写在"+downloadList+"文件");
					System.out.println("例如："+url);
                }

            }
        //   Thread.sleep(2000);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println(Thread.activeCount());
            while (Thread.activeCount()>1);
            System.out.println(Thread.activeCount());			
        }

    }

    public void dowloadM3u8(String finalUrl, String baseDir) {
        try {
            //url = "https://tspc.vvvdj.com/c1/2021/12/224514-b9caed/224514.m3u8?upt=a88e0ee91643471999&news";
            if (finalUrl==null) return;
	    String url = finalUrl.split("##")[0];
	    String musicName = finalUrl.split("##")[1];
            System.out.println("当前进程的工作空间:" + System.getProperty("user.dir"));
			
            String baseUrl = url.substring(0, url.lastIndexOf("/") + 1);
            String fileName = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("?"));
            String id = fileName.substring(0, fileName.lastIndexOf("."));
//            String sourceDir = "D:" + PATH_SEPARATOR + "Desktop" + PATH_SEPARATOR + "Desktop";
            //String sourceDir =baseDir+ PATH_SEPARATOR + "Source";
            String sourceDir =baseDir+ PATH_SEPARATOR + "Music"+ PATH_SEPARATOR+date;
            String downloadDir = sourceDir + PATH_SEPARATOR + id;


            String downloadFile = downloadDir + PATH_SEPARATOR + fileName;
            //String outfileDir = baseDir+ PATH_SEPARATOR + "Music";
            String outfileDir = baseDir+ PATH_SEPARATOR + "Music"+ PATH_SEPARATOR+date;
            //String outfileDir = "/usr/local/nginx/html/qingfeng/"+date;
            String outfile = outfileDir+PATH_SEPARATOR + id+"_"+musicName + ".aac";
//            Properties properties = System.getProperties();
//            System.out.println("properties = " + properties);
            if (new File(outfile).exists()){
                System.out.println(outfile+"   exists!");
                return;
            }

            
            File dir = new File(downloadDir);
            if (!dir.exists()) dir.mkdirs();
			
	    dir = new File(outfileDir);
            if (!dir.exists()) dir.mkdirs();


            File m3u8 = new File(downloadFile);
            if (!m3u8.exists())
                downloadFile(url, downloadFile);
			
			//存储ts文件
			List<String> filetxtList = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader(m3u8));
            String len = null;
            while ((len = br.readLine()) != null) {
                if (len != null && len.startsWith("#")) continue;
                String tmpurl = baseUrl + len;
                String filename = len.substring(0, len.lastIndexOf("?"));
                String tempfilepath = downloadDir + PATH_SEPARATOR + filename;
                //System.out.println("tmpurl = "+filename+":"+ new File(tempfilepath).length());
                File file = new File(tempfilepath);
                if (!file.exists() || file.length() <= 0)
                    downloadFile(tmpurl, tempfilepath);
				filetxtList.add(tempfilepath);
            }
            br.close();


            //保存ts列表id.txt文件名
            String filetxt = downloadDir + PATH_SEPARATOR + id + ".txt";
            File ftxt = new File(filetxt);
            if (!ftxt.exists() || ftxt.length() <= 0) {
                /*
                File[] tsList = new File(downloadDir).listFiles(file -> file.isFile()&& file.getName().endsWith(".ts"));
                Arrays.sort(tsList);
                BufferedWriter bw = new BufferedWriter(new FileWriter(ftxt));
				for (File f : tsList) {
                    String name = f.getAbsoluteFile().toString();
                    bw.write("file '" + name+"'");
                    bw.newLine();
                }
				*/
				BufferedWriter bw = new BufferedWriter(new FileWriter(ftxt));
				int size=filetxtList.size();
				for(int i=0;i<size;i++){
					bw.write("file '" + filetxtList.get(i)+"'");
                    bw.newLine();
				}
                bw.close();
                System.out.println(filetxt+" 保存成功！");
            }



            //ts合成
            String cmd = baseDir+PATH_SEPARATOR+"ffmpeg -f concat -safe 0 -i " + filetxt + " -acodec copy "+outfile;
            System.out.println("cmd = " + cmd);
            ProcessBuilder pb = new ProcessBuilder().command( "sh","-c",cmd ).inheritIO();
            //pb.redirectErrorStream(true);//这里是把控制台中的红字变成了黑字，用通常的方法其实获取不到，控制台的结果是pb.start()方法内部输出的。
            //pb.redirectOutput(tmpFile);//把执行结果输出。
            pb.start().waitFor();//等待语句执行完成，否则可能会读不到结果。
            System.out.println("执行完成");
            if(new File(outfile).exists()){
            System.out.println(outfile+" 保存成功！");
			/*
			File oldfile=new File(outfile);
			File newfile = new File(sourceDir+ PATH_SEPARATOR + id + ".aac");
			oldfile.renameTo(newfile);
			*/
            //删除ts文件
			
           /* File[] tsList = new File(downloadDir).listFiles(o -> o.isFile()&& o.getName().endsWith(".ts"));
            for (File f:tsList){
                f.delete();
            }
			System.out.println("删除ts文件完成");
           */
	   }else{
	   
            System.out.println(outfile+" 保存失败，请检查！");
	   }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void downloadFile(String url, String downloadFile) {
        OutputStream os = null;
        InputStream is = null;
        try {
            URL u = new URL(url);
            os = new FileOutputStream(downloadFile);
            is = u.openStream();

            int len = 0;
            byte[] b = new byte[1024];
            while ((len = is.read(b)) != -1) {
                os.write(b, 0, len);
            }
            System.out.println(url + "  download success!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(url + "  download field!");
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
