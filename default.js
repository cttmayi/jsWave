var result = new java.util.ArrayList();

function s(t) {
	return Number(1000000 * t);
}

function ms(t) {
	return Number(1000 * t);
}

function us(t) {
	return Number(1 * t);
}

function color(r,g,b) {
	return Number(r * 256 * 256 + g * 256 + b)
}

function ArrayList() {
	var list = new java.util.ArrayList();

	var i
	for (i=0; i<arguments.length;i++) {
		list.add(arguments[i])
	}
	return list;
} 

function Script() {
	return Packages.jswave.js.Script.getScript();
}

var black = color(0,0,0)
var red = color(255,0,0)
var yellow = color(255,255,0)
var green = color(0,255,0)
var white = color(255,255,255)


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


function line_0() {
	var times = ArrayList();
	var colors = ArrayList();	
	var names = ArrayList();
	var ys = ArrayList();
	
	
	times.add((s(1))); colors.add(black); names.add(""); ys.add(0)
	times.add(s(2)); colors.add(yellow); names.add("S");ys.add(3)
	times.add(s(3)); colors.add(red); names.add("R");ys.add(0)
	times.add(s(3.5)); colors.add(green); names.add("R");	ys.add(0)
	
	var script = Script();
	script.addLine("line_1ABCDEFG1234\nABCDEFG", times, colors, ys, names);
	
}


function main(){
	var script = Script();
	
	script.setTimeRuler(3600 * 25);
	
	script.setRangeListener("range");
	script.setSelectListener("range");
	script.setConnectionListener("connection");
	script.setWaveListener("wave");
	script.setClickListener("click");
	
	histogram_0()
	histogram_0()
	line_0()
	line_0()

	script.addConnection(s(1), 1, 2, 0);
	script.addConnection(s(2), 2, 3, 0);

	script.addConnection(s(1.8), 0, 3, 0);
	//script.addConnection(s(2), 9, 3, 0);
	//script.addConnection(s(2.5), 22, 6, 0);	
	

}

function range(id, t1, t2){

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
	infoShow("show " + id + "\n\nMK", t, l)
	
}

function wave(id, t, line) {
	infoShow("line: " + line + "\nid: " + id, t, line)
}

function click(id, t, line) {
	infoShow("click " + id, t, line)
}
main()