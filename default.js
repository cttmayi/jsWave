

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

function node_0() {
	var script = Script();
	var hs = ArrayList(5, 20);
	var colors = ArrayList(dark_yellow, dark_red);	
	var ys = ArrayList(0, 5);
	return script.addNode(hs, ys, colors, red);
}

function node_1() {
	var script = Script();
	var hs = ArrayList(25);
	var colors = ArrayList(dark_red);	
	var ys = ArrayList(0);
	return script.addNode(hs, ys, colors, red);
}

function node_2() {
	var script = Script();
	var hs = ArrayList(25);
	var colors = ArrayList(dark_yellow);	
	var ys = ArrayList(0);
	return script.addNode(hs, ys, colors, yellow);
}

function nodes_histogram() {
	var script = Script();
	
	var n_0 = node_0();
	var n_1 = node_1();
	var n_2 = node_2();
	
	
	
	var stime = ArrayList();
	var etime = ArrayList();
	var nodes = ArrayList();
	var names = ArrayList();
	
	stime.add(s(1)); etime.add(s(2)); nodes.add(n_0); names.add("1")
	stime.add(s(3)); etime.add(s(5)); nodes.add(n_1); names.add("2")
	stime.add(s(5.1)); etime.add(s(6)); nodes.add(n_2); names.add("3")
	stime.add(s(6)); etime.add(s(7.1)); nodes.add(n_0); names.add("4")
	return script.addNodes("histogram", 50, stime, etime, nodes, names);
}

function node_sleep(h) {
	var script = Script();
	var hs = ArrayList(1);
	var colors = ArrayList(dark_yellow);	
	var ys = ArrayList(h/2);
	return script.addNode(hs, ys, colors, dark_yellow);
}

function node_run(h) {
	var script = Script();
	var hs = ArrayList(1);
	var colors = ArrayList(green);	
	var ys = ArrayList(h-3);
	return script.addNode(hs, ys, colors, green);
}

function node_runable(h) {
	var script = Script();
	var hs = ArrayList(h-4);
	var colors = ArrayList(dark_red);	
	var ys = ArrayList(2);
	return script.addNode(hs, ys, colors, red);
}



function nodes_line() {
	var script = Script();
	
	var h = 20
	var sleep = node_sleep(h);
	var run = node_run(h);
	var runable = node_runable(h);

	var stime = ArrayList();
	var etime = ArrayList();
	var nodes = ArrayList();
	var names = ArrayList();
	
	stime.add(ms(1)); etime.add(ms(4)); nodes.add(sleep);
	stime.add(ms(4)); etime.add(ms(5)); nodes.add(runable);
	stime.add(ms(5)); etime.add(ms(6)); nodes.add(run);
	stime.add(ms(6)); etime.add(ms(7.1)); nodes.add(sleep);
	
	
	stime.add(s(1)); etime.add(s(3)); nodes.add(sleep);
	stime.add(s(3)); etime.add(s(5)); nodes.add(runable);
	stime.add(s(5)); etime.add(s(6)); nodes.add(run);
	stime.add(s(6)); etime.add(s(7.1)); nodes.add(sleep);
	

	var lid = script.addNodes("line", h, stime, etime, nodes, names);
	script.setLineConnctionY(lid, h/2+3, h/2-3);
	return lid
}

function main(){
	var script = Script();
	
	script.setTitle("Time Range A");
	script.setPanelXOffset(150);
	script.debug(1);
	
	script.setTimeRuler(s(3600 * 25) + ms(550), s(2333) + ms(670));
	script.setTimePoint(s(8), yellow)
	
	script.setRangeListener("range");
	script.setSelectListener("selectWave");
	script.setConnectionListener("connection");
	script.setWaveListener("wave");
	script.setClickListener("click");
	script.setNameListener("name");
	script.addKeyListener("Q", "key_Q");

	nodes_histogram()
	nodes_histogram()
	
	nodes_line()
	nodes_line()
	nodes_line()

	script.addConnection(s(1), 0, 1, white);
	script.addConnection(s(2), 1, 0, white);
	script.addConnection(s(3.2), 2, 3, yellow);
	script.addConnection(s(4.2), 3, 2, yellow);
	
	script.addGroup("MORE", 0,1, blue,true)
	
	script.setText("Message is too long")
}

function showTable() {
	
	var script = Script();
	var names = ArrayList("TTTTTTTTTTT1", "T2", "T3", "T4", "T5");
	var data = ArrayList("111111111111", "54343", "3", "4", "5");
	var datas = ArrayList(names, data, data, data, data);
	
	var fg = ArrayList(red, yellow)
	var fgs = ArrayList(fg, fg, fg)

	var bg = ArrayList(yellow, red)
	var bgs = ArrayList(bg, bg)
	
	script.setTable(datas, fgs, bgs);
	
}


function range(id, t1, t2, b){

	var LEFT = 1
	var RIGHT = 3
	var script = Script();
	//script.print(b)
	

	if (b == RIGHT && t2 - t1 > 800){
		script.setTimeRange(t1, t2 - t1);
	}
	else if (b == LEFT) {
		showTable()
		script.setText("MK")
	}
	script.setText("select wave range")
}

function selectWave(id, t1, t2){
	script.setText("select wave")
}

function connection(id, t, l) {
	if (id >= 0) {
		infoShow("show " + id + "\n\nshow", t, l)
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
}
main()