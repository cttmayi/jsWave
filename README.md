功能:
- 以时间轴位, 显示图像关系图
- 输入JavaScript文件, 执行文件, 输出图形关系图


编译环境:
- 使用netbean编译

执行环境:
- JDK 1.6 或者以上
- java -jar jsWave.jar


目录解决:
- src: java 代码
- libs: js库文件
- default.js 测试文件


JavaScript API:
- int addLine(String name, ArrayList<Number> times, ArrayList<Number> colors, ArrayList<Number> ys, ArrayList<String> names)
 - 添加Line, 返回行数
- int addHistogram(String name, ArrayList<Number> times, ArrayList<Number> times2, ArrayList<Number> ys, ArrayList<Number> colors, ArrayList<String> names, int heightMax)
 - 添加Histogram, 返回行数
- int addConnection(double time, double start, double end, double color)
 - 添加行与行的链接
- void setTable(ArrayList<String> names, ArrayList<String> datas, ArrayList<String> datars)
- void setInfo(ArrayList<Number> xs, ArrayList<Number> ys, ArrayList<String> names, ArrayList<Number> fgcs, ArrayList<Number> bgcs)
- void setTimeRuler(Number s)
 - 设置Time Ruler 的时间偏移量.
- void setRangeListener(String name)
- void setSelectListener(String name)
- void setConnectionListener(String name)
- void setWaveListener(String name)
- void setClickListener(String name)
