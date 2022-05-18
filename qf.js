const {JSDOM} = require('jsdom');
 !(async()=>{
	
	var str = await getList("https://www.vvvdj.com/sort/c1/");
	//var reg = /<td><input class="sortid" name="id" type="checkbox" checked  value="(\d+)"><\/td>/g;
	//var result = str.match(reg);	
    
	//var ids = result.map(item=>{
        //let id= /value="(\d+)"/g.exec(item)[1];
        //return id;
	//});
	const dom = new JSDOM(str);
	let table = dom.window.document.querySelector('.list_musiclist');
	let trs = table.getElementsByTagName("tr");
	let item = [];
	const date=new Date();
	let y = date.getFullYear();
	let m = (date.getMonth()+1);
	let d = date.getDate();
	let tmpdate = y+'/'+(m>9?m:'0'+m)+'/'+(d>9?d:'0'+d)
	
	let ids = [];
	for(let i = 1 ;i<trs.length;i++){
		let tds  = trs[i].getElementsByTagName("td")
		let id = tds[1].textContent;
		//let name  = tds[2].querySelector('.t1').textContent;
		let datetime  = tds[4].textContent;	
		//console.log(datetime,tmpdate);
		if(datetime == tmpdate){
			ids.push(id);
			//console.log(id,datetime+name)		
		}
	}


   //console.log(str);
   //console.log(ids);
//return;
	for(let i = 0 ;i<ids.length;i++){
		(function(i) {
			setTimeout(async function() {			
				let url = await getDownloadUrl(ids[i]);
				console.log(url);
			}, (i + 1) * 2000);
        })(i)
	}
	
 })()


 
function  getList(url){
	return new Promise((resolve) => {
    require('request')(url,(error, response, body)=>{			
		resolve(body)		
	});
	});
 }
function  geturl(musicId){
	return new Promise((resolve) => {
    require('request')('https://www.vvvdj.com/play/'+musicId+'.html',(error, response, body)=>{		
	if(error)console.log(error);
	var str = response.body;
	var regq = /playurl=x\.O000O0OO0O0OO\('(.*)','(.*)'\)/g;
	var result = regq.exec(str);	
	var regname = /<h1>(.*)<\/h1>/g;
	result2 = regname.exec(str);
	//console.log(result2[1]);
    resolve([result[1],result[2],result2[1]])	
	//playurl=x.O000O0OO0O0OO('e0A1092112b115Z121l127e130:136^139p145s148W154[163;166T169n172r175=178V1902193D196D199Q214D2171223A2293238=241N247R250N253Q265E271L277M2891292^295U3014310E316g322=328Q337:343P346>349=352=385V388P394r400E403e418V421X430a460r466t469E475N481L484B487X490F493>502F505Q511K514A523Q526D529:541Q5505556>571?574F577J580s583Y589c604u607','ZnS5PxAehgEFFIjpoJa9LHEmwK2NhI3YVaWmirVa0dScY6ExmriAo9r1TveRJZe5XYtZj78QahYHlpsMBmLDjBVZHVHPZHGwwrVwWKTzjUt6RqbBp2jRzFlMYttDMxihLzZqEHZqpeccLmThxhA24mO2288fIESnxVMP4tAOwZvwgLGi2krsgvYRFD68Fh550M1hntRE3hyWWtL0MT6GdVFLKzllbw9ciwZxnJjKQTKREoJNIksPqSdE3TyO1wXebVqh1SmiwrR7t3eKiScLMwEhZSu40we5Nx3bZRsU78UOQf9i8FjCg0BzjXR7TkUqGr72FnlRxV0Jh9anKewo6REQFbEpQGtXlVUc00KZfXtcPbTzX1xqGsxvwwkgOSf3xnoFhbJr5a1TUpKhM6OiFNjyi4Eg59PLrzZd57WIPRHRS4AW5NZ3CBJMi3ZyF0AyXevr8uslFqQ68NcEixd833Qnr61Cv5evtJKT8PV2vHMJqGrjRFh1su7LKzn07N1v');
//    播放全部

	});
	});
 }
 

 
function getDownloadUrl(mId){
	return new Promise(async (resolve) => {
    let args = await geturl(mId);	  
	let playurl = new DeCode().O000O0OO0O0OO(args[0],args[1]);	 
	
	resolve('https:'+playurl+'##'+args[2]);	
	});
 }
 
 
 function DeCode() {
        this.OO0O00OO00OO = function (a, b) {
            return b > 0 ? a.substring(0, b) : null;
        }, this.OO00OO0O00O0 = function (a, b) {
            return a.length - b >= 0 && a.length >= 0 && a.length - b <= a.length ? a.substring(a.length - b, a.length) : null;
        }, this.O0000OO0OO00O0 = function (a, b) {
            var c, d, e, f, g, h, i, j, k = "";
            for (c = 0; c < b.length; c++) {
                k += b.charCodeAt(c).toString();
            }
            for (d = Math.floor(k.length / 5), e = parseInt(k.charAt(d) + k.charAt(2 * d) + k.charAt(3 * d) + k.charAt(4 * d) + k.charAt(5 * d)),
                     f = Math.round(b.length / 2), g = Math.pow(2, 31) - 1, h = parseInt(a.substring(a.length - 8, a.length), 16),
                     a = a.substring(0, a.length - 8), k += h; k.length > 10; ) {
                k = (parseInt(k.substring(0, 10)) + parseInt(k.substring(10, k.length))).toString();
            }
            for (k = (e * k + f) % g, i = "", j = "", c = 0; c < a.length; c += 2) {
                i = parseInt(parseInt(a.substring(c, c + 2), 16) ^ Math.floor(255 * (k / g))), j += String.fromCharCode(i),
                    k = (e * k + f) % g;
            }
            return j;
        }, this.O0000OO0OO00O = function (a, b, c) {
            return a.length >= 0 ? a.substr(b, c) : null;
        }, this.O0O000000O0O0 = function (a) {
            return a.length;
        }, this.O000O0OO0O0OO = function (a,b) {           
            var h, i, j, k, l, m, n, o, p, c = b, d = this.O0O000000O0O0(c), e = d, f = new Array(), g = new Array();
            for (l = 1; d >= l; l++) {
                f[l] = this.O0000OO0OO00O(c, l - 1, 1).charCodeAt(0), g[e] = f[l], e -= 1;
            }
            for (h = "", i = a, m = this.OO0O00OO00OO(i, 2), i = this.OO00OO0O00O0(i, this.O0O000000O0O0(i) - 2),
                     l = 0; l < this.O0O000000O0O0(i); l += 4) {
                j = this.O0000OO0OO00O(i, l, 4), "" != j && (b = this.OO0O00OO00OO(j, 1), k = (parseInt(this.OO00OO0O00O0(j, 3)) - 100) / 3,
                    m == this.O0000OO0OO00O0("a9ab044c634a", "O0000OO0OO00O") ? (n = 2 * parseInt(b.charCodeAt(0)),
                        o = parseInt(f[k]), p = n - o, h += String.fromCharCode(p)) : (n = 2 * parseInt(b.charCodeAt(0)),
                        o = parseInt(g[k]), p = n - o, h += String.fromCharCode(p)));
            }
            return h;
        };
 }


