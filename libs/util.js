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

