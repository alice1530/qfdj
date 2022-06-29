import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
public class createHtml{
   
public static final String PATH_SEPARATOR = File.separator;

public static void main(String[] args){

    String dir = System.getProperty("user.dir")+PATH_SEPARATOR+"Music"+PATH_SEPARATOR;
//String dir="/root/qfdj/Music/";
//String date = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis()-3600*24*60);
String date = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());

try (FileWriter fw = new FileWriter(dir+date+"/list"+date+".html");FileWriter wf = new FileWriter(dir+"index.html")) {
	StringBuilder sb = new StringBuilder();
        //sb.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"></head>");
        //sb.append("<body>");
	sb.append("<div style=\"text-align:center;background-color:yellow;margin:10px;padding:5px;\">");
        sb.append("<span>"+date+"</span>");
       // sb.append("<audio id=\""+date+"\"  style=\"height: 20px;\"autoplay controls ></audio>");
        sb.append("</div>");
	//sb.append("<div  style=\"display: flex;flex-direction: row;flex-wrap: wrap;justify-content: space-evenly;\">");
        sb.append("<ol>");
	//sb.append("<ol  style=\"display:flex;flex-wrap:wrap;justify-content:flex-start;flex-direction:column;height:200px;align-content:space-around;\">");
	File f = new File(dir+date);
	String[] list = f.list();
        Arrays.sort(list,Collections.reverseOrder());
	for (int i = 0; i < list.length; i++)
		if (list[i].endsWith(".aac")){
	                sb.append("<li class=\"item\"> ");
			sb.append("<a id=\""+list[i].split("_")[0]+"\" onclick=\"play()\" href=\"javascript:void(0);\">"+list[i]+ "</a>");
	                sb.append("</li>\n");
                 }
	sb.append("</ol>");
	fw.write(sb.toString());
	fw.flush();
	fw.close();
	File fm = new File(dir);
	File[] listm = fm.listFiles();
	StringBuilder frame = new StringBuilder();
        frame.append("<html>");
        frame.append("<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">");
        frame.append("<style type=\"text/css\">.item:nth-child(odd){border:solid 1px;width:580px;background-color:#ffc0cb3b;margin:1px;}</style>");
	frame.append("<style type=\"text/css\">.item:nth-child(even){border:solid 1px;width:580px;background-color:#aacaff3b;}</style>");
        frame.append("<style type=\"text/css\">.current{border: solid 1px;width:725px;font-size:initial;text-align:center;background-color:greenyellow;");
	frame.append("animation-name:current_ant;animation-duration: 2s;animation-iteration-count: infinite;animation-direction: alternate;}</style>");
        frame.append("<style type=\"text/css\">@keyframes current_ant{from{transform:scale(1,1)} to{transform:scale(0.8,0.8)}}</style>");
        frame.append("<style type=\"text/css\">h1{cursor: pointer;} a{text-decoration:none;font-size:larger;}</style>");
        frame.append("<style type=\"text/css\">ol{display: flex;flex-direction: column; align-items: center;}</style>");
        frame.append("<link rel=\"stylesheet\" href=\"video-js.min.css\">");
        frame.append("<script src=\"video.min.js\"></script>");
        frame.append("<title>在线试听</title>");
        frame.append("</head><body style='font-size:smaller;background-color:aliceblue;'>");
        frame.append("<h1 onclick='dspl()' id=\"showName\" style = \"color:red;text-align: center;\">点击列表↓↓↓↓播放音乐</h1>");
       // frame.append("<div id='showView' style='display:none;justify-content: center;'>");
        frame.append("<div id='showView' style='display: none; flex-direction: column; align-items: center; justify-content: flex-start;'>");
	frame.append("<video id='my-player'  class='video-js vjs-big-play-centered'></video>");
	frame.append("<div><input id='range' type='range' value='1' min='0.1' max='2' step='0.1' onchange='changeV()'>");
	frame.append("<br>倍速:<label id='label' for='range'>1</label></div>");
	frame.append("</div>");
        frame.append("<h1 id='control'  style='display:none;color:red;text-align: center;'>");
        frame.append("<a title='ctrl+←' href='javascript:void(0);' onclick='prevNext(-1)'>←上一首</a>&nbsp;&nbsp;|&nbsp;&nbsp;");
        frame.append("<a title='space' href='javascript:void(0);' onclick='continuePlay()'></a>&nbsp;&nbsp;|&nbsp;&nbsp;");
        frame.append("<a title='ctrl+→' href='javascript:void(0);' onclick='prevNext(1)'>下一首→</a></h1>");
        frame.append("<h1 id=\"showDowload\" style = \"color:red;text-align: center;\"></h1>");


	//加载列表
	Arrays.asList(listm).stream().filter( o-> o.isDirectory()&&o.getName().startsWith("20")).map(o->o.getName()).sorted(Comparator.reverseOrder()).forEach( dname -> {
            		
			try{
				BufferedReader br = new BufferedReader(new FileReader(dir+dname+"/list"+dname+".html"));
            			String len = null;
           			while ((len = br.readLine()) != null) {
					frame.append(len);
            			}
            			br.close();
			}catch (Exception e){
				e.printStackTrace();
			}finally{
				//System.out.println(dname);
			}
		}
);
        frame.append("<div>每日13点10分更新，仅保留近7天的内容</div>");
        frame.append(" <script type=\"text/javascript\">");
	//初始参数
        frame.append("let swich=true; ");
        frame.append("let rate = 1;");
        frame.append("let btn = document.getElementById('control').children[1];");
	//页面加载完成
        frame.append("window.onload=function(){ ");
        	frame.append("player = videojs('my-player', {autoplay:true,controls:true,preload:\"auto\"},function(){");
			frame.append("this.on('ended',()=>{");
			frame.append("prevNext(1);");
			frame.append("});");
			frame.append("this.on('waiting',()=>{");
			frame.append("document.querySelector('.current').style.animationPlayState = 'paused';");
			frame.append("btn.innerText ='加载中...';");
			frame.append("});");
			frame.append("this.on('play',()=>{");
			frame.append("player.playbackRate(rate);");
			frame.append("});");
			frame.append("this.on('playing',()=>{");
			frame.append("document.querySelector('.current').style.animationPlayState = 'running';");
			frame.append("btn.innerText ='播放中';");
			frame.append("});");
			frame.append("this.on('pause',()=>{");
			frame.append("document.querySelector('.current').style.animationPlayState = 'paused';");
			frame.append("btn.innerText ='已暂停';");
			frame.append("});");
		frame.append("});");
        frame.append("};");
	//倍速
        frame.append("function changeV(){");
	frame.append("rate = document.getElementById('range').value;");
	frame.append("document.getElementById('label').innerText=rate;");
	frame.append("player.playbackRate(rate);");
	frame.append("};");
	//播放
        frame.append("function play() {");
        frame.append("var aa=event.target;");
        frame.append("var id = aa.id;");
        frame.append("var date = aa.parentElement.parentElement.previousElementSibling.innerText;");
        frame.append("var name = aa.textContent;");
        frame.append("if(document.querySelector('.current')!=null)document.querySelector('.current').setAttribute('class','item');");
        frame.append("document.getElementById(id).parentElement.setAttribute('class','current');");
        frame.append("document.getElementById('showName').innerHTML=name;");
        frame.append("document.getElementById('showDowload').innerHTML=\"<a target='_blank' href='\"+date+\"/\"+name+\"'>↓下载↓</a>\";");
        frame.append("player.src([{src: date+\"/\"+id+\"/\"+id+\".m3u8\",type: \"application/x-mpegURL\"}]);");
        frame.append("document.getElementById('showName').scrollIntoView({ behavior: 'smooth'});");
        frame.append("document.getElementById('control').style.setProperty('display','block');");
        frame.append("};");
	//显示播放窗口
        frame.append("function dspl() {");
        frame.append("if (swich=!swich)");
        frame.append("document.getElementById('showView').style.setProperty('display','none');");
        frame.append("else ");
        frame.append("document.getElementById('showView').style.setProperty('display','flex');");
        frame.append("};");
	//下一首，上一首
        frame.append("function prevNext(num) {");
	frame.append("var list = document.querySelectorAll('a[id]');");
	frame.append("var nextIndex=0;");
	frame.append("let str = player.currentSrc();");
	frame.append("let id = str.substr(str.lastIndexOf('/')+1,str.length-str.lastIndexOf('.')+1);");
	frame.append("list.forEach((item,index,arr)=>{if(item.id == id)nextIndex=index+num;});");
	frame.append("nextIndex=nextIndex<0?list.length-1:nextIndex;");
	frame.append("nextIndex=nextIndex>list.length-1?0:nextIndex;");
	frame.append("list[nextIndex].click();");
        frame.append("};");
	//暂停，播放
        frame.append("function continuePlay() {");
        frame.append("if(player.paused())player.play();else player.pause();");
        frame.append("};");
	//键盘事件
        frame.append("document.addEventListener('keydown',e=>{");
        frame.append("if(e.ctrlKey&&e.keyCode===37)prevNext(-1);");
        frame.append("else if(e.ctrlKey&&e.keyCode===39)prevNext(1);");
        frame.append("else if(e.ctrlKey&&e.keyCode===38){var tvol=player.volume()+0.1;player.volume(tvol>1?1:tvol)}");
        frame.append("else if(e.ctrlKey&&e.keyCode===40){var tvol=player.volume()-0.1;player.volume(tvol<0?0:tvol)}");
        frame.append("else if(e.keyCode===32)continuePlay();");
        frame.append("});");
        frame.append("</script>");
        frame.append("</body>");
        frame.append("</html>");
        wf.write(frame.toString());
	wf.flush();
} catch (Exception e) {
	e.printStackTrace();
}

}

}
