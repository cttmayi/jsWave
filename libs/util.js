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
	return Number(100 * 256 * 256 * 256 +  r * 256 * 256 + g * 256 + b)
}

function color_a(r,g,b, a) {
	return Number(a * 256 * 256 * 256 +  r * 256 * 256 + g * 256 + b)
}

function Script() {
	return Packages.jswave.js.Script.getScript();
}

function ArrayList() {
	var list = new java.util.ArrayList();

	var i
	for (i=0; i<arguments.length;i++) {
		list.add(arguments[i])
	}
	return list;
}

var black = color(0,0,0)
var dark_red = color(64,0,0)
var red = color(255,0,0)
var yellow = color(255,255,0)
var dark_yellow = color(192,192,0)
var green = color(0,255,0)
var dark_green = color(0,128,0)
var white = color(255,255,255)
var blue = color(0,0,255)
var dark_blue = color(32,32,112)

function key_W() {
	Script().setTimeRange(Script().getTimeOffset(),  Script().getTimeWidth() * 3/4 );
}

function key_S() {
	Script().setTimeRange(Script().getTimeOffset(), Script().getTimeWidth() * 4/3);
}

function key_A() {
	Script().setTimeRange(Script().getTimeOffset() - Script().getTimeWidth()* 1/10, Script().getTimeWidth());
}

function key_D() {
	Script().setTimeRange(Script().getTimeOffset() + Script().getTimeWidth()* 1/10, Script().getTimeWidth());
}

Script().addKeyListener("W", "key_W");
Script().addKeyListener("S", "key_S");
Script().addKeyListener("A", "key_A");
Script().addKeyListener("D", "key_D");

