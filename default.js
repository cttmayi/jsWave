

function infoShow(n, x, y) {
	var xs = ArrayList();
	var ys = ArrayList();
	var names = ArrayList();
	var fgs = ArrayList();
	var bgs = ArrayList();

	xs.add(Number(x)); ys.add(Number(y)); names.add(n);  fgs.add(red); bgs.add(green);

	var script = Script();
	script.setInfo(xs, ys, names, fgs, bgs);
}

function infoClear() {
	var empty = ArrayList();
	var script = Script();
	script.setInfo(empty, empty, empty, empty, empty);
}

function histogram_0() {
	var time = ArrayList();
	var time2 = ArrayList();
	var colors = ArrayList();
	var ys = ArrayList();
	var names = ArrayList();
	
	time.add(ms(1)); time2.add(ms(1)+us(1)); ys.add(40.0);  colors.add(red); names.add("3");
	time.add(ms(2)); time2.add(ms(3)); ys.add(40.0);  colors.add(red); names.add("3");

	time.add(ms(4)); time2.add(ms(5)); ys.add(50.0);  colors.add(green); names.add("3");
	time.add(s(2)); time2.add(s(3)); ys.add(40.0);  colors.add(yellow); names.add("3");
	time.add(s(4)); time2.add(s(13)); ys.add(40.0); colors.add(yellow); names.add("2");
	time.add(s(40)); time2.add(s(73)); ys.add(30.0); colors.add(red); names.add("2");

	var script = Script();
	script.addHistogram("histogram_0", time, time2, ys, colors, names, 50);
}


function histogram_1() {
	var time = ArrayList();
	var time2 = ArrayList();
	var colors = ArrayList();
	var ys = ArrayList();
	var names = ArrayList();
	
	time.add(ms(-1000)); time2.add(ms(1.5)); ys.add(40.0);  colors.add(red); names.add("3");
	//time.add(ms(2)); time2.add(ms(3)); ys.add(40.0);  colors.add(red); names.add("3");
	
	//time.add(ms(4)); time2.add(ms(4)); ys.add(40.0);  colors.add(red); names.add("3");
	time.add(s(17)+ms(5)); time2.add(s(17)+ms(5)); ys.add(40.0);  colors.add(red); names.add("3");
	//time.add(ms(4)); time2.add(ms(5)); ys.add(50.0);  colors.add(green); names.add("3");
	//time.add(s(2)); time2.add(s(3)); ys.add(40.0);  colors.add(yellow); names.add("3");
	//time.add(s(4)); time2.add(s(13)); ys.add(40.0); colors.add(yellow); names.add("2");
	//time.add(s(40)); time2.add(s(73)); ys.add(30.0); colors.add(red); names.add("2");

	var script = Script();
	return script.addHistogram("histogram_1", time, time2, ys, colors, names, 50);
}

function line_0() {
	var times = ArrayList();
	var colors = ArrayList();	
	var ys = ArrayList();
	
	
	times.add((s(1))); colors.add(black); ys.add(30)
	times.add(s(2)); colors.add(yellow); ys.add(42)
	times.add(s(3)); colors.add(red); ys.add(4)
	times.add(s(3.5)); colors.add(green); ys.add(4)
	
	var script = Script();
	script.addLine("line_1", times, colors, ys);
	
}


function main(){
	var script = Script();
	
	script.setTitle("Time Range A");
	script.debug(1);
	
	script.setTimeRuler(s(3600 * 25) + ms(550), s(2333) + ms(670));
	script.setTimePoint(s(8), yellow)
	
	script.setRangeListener("range");
	script.setSelectListener("select");
	script.setConnectionListener("connection");
	script.setWaveListener("wave");
	script.setClickListener("click");
	script.setNameListener("name");
	script.addKeyListener("Q", "key_Q");
	
	var l = histogram_1()
	script.setWaveOutBorderColor(l, black)
	histogram_0()
	line_0()
	line_0()
	line_0()
	line_0()

	script.addConnection(s(1), 1, 2, white);
	script.addConnection(s(2), 2, 3, black);

	script.addConnection(s(1.8), 0, 3, red);
	//script.addConnection(s(2), 9, 3, 0);
	//script.addConnection(s(2.5), 22, 6, 0);	
	
	script.addGroup("ANR", 0,0,yellow,true)
	script.addGroup("Message", 1,2,yellow,true)
	script.addGroup("TRACE", 3,4,green,false)
	
	script.setText("Message is too long")
}

function range(id, t1, t2, b){

	var LEFT = 1
	var RIGHT = 3
	var script = Script();
	script.print(b)
	
	if (b == LEFT) {
		var name = ArrayList("T", String(id))
		var d = ArrayList()
		d.add("T1")	
		d.add(String(t1))	
		var d2 = ArrayList()
		d2.add("T2")
		d2.add(String(t2))

		script.setTable(name, d, d2)
		script.setText("MK")
	}
	else {
		script.setTimeRange(t1, t2 - t1);
	}
}

function select(id, t1, t2){

	var name = ArrayList("T", String(id))
	var d = ArrayList()
	d.add("T1")	
	d.add(String(t1))	
	var d2 = ArrayList()
	d2.add("T2")
	d2.add(String(t2))
	
	var script = Script();
	script.setTable(name, d, d2)
	script.setText("MK")
}

function connection(id, t, l) {
	if (id >= 0) {
		infoShow("show " + id + "\n\nMK", t, l)
	}
	else {
		infoClear()
	}
	//Script().clearConnection()
	
}

function wave(id, t, line) {
	if (id >= 0) {
		infoShow("line: " + line + "\nid: " + id, t, line)
	}
	else {
		infoClear()
	}
}

function click(id, t, line) {
	if (id >= 0) {
		infoShow("click " + id, t, line)
	}
	else {
		infoClear()
	}	
	//Script().clearWave()
}

function name(t, line) {
	if (line >= 0) {
		infoShow("name " + line, t, line)
	}
	else {
		infoClear()
	}
}

function key_Q() {
	histogram_1()
}
main()