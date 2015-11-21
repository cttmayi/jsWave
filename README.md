功能:
- 以时间轴位, 显示图像关系图
- 输入JavaScript文件, 执行文件, 输出图形关系图


编译环境:
- 使用netbean编译

执行环境:
- JDK 1.6 或者以上
- java -jar jsWave.jar


目录结构:
- src: java 代码
- libs: js库文件
- default.js 测试文件


JavaScript API:
- int addLine(String name, ArrayList<Number> times, ArrayList<Number> colors, ArrayList<Number> ys, ArrayList<String> names)
 - 添加Line, 返回行数
- int addHistogram(String name, ArrayList<Number> times, ArrayList<Number> times2, ArrayList<Number> ys, ArrayList<Number> colors, ArrayList<String> names, int heightMax)
 - 添加Histogram, 返回行数
- int addConnection(double time, double start, double end, double color)
 - 添加行与行的连接， 返回ID号
- void setTable(ArrayList<String> col0, ArrayList<String> col1, ArrayList<String> col2)
 - 显示Table数据
- void setInfo(ArrayList<Number> xs, ArrayList<Number> ys, ArrayList<String> names, ArrayList<Number> fgcs, ArrayList<Number> bgcs)
 - 显示TEXT数据
- void setTimeRuler(Number s)
 - 设置 Time Ruler 的时间偏移量.
- void setRangeListener(String callback)
 - callback(l, t1, t2)
- void setSelectListener(String callback)
 - callback(l, t1, t2)
- void setConnectionListener(String callback)
 - callback(id, t, l), 鼠标移动到Connection附近. 输入id号, 时间位置, 行数位置.
- void setWaveListener(String callback)
 - callback(l, id, t), 鼠标移到到Wave特定的Item上. 输入id号, 时间位置, 行数位置.
- void setClickListener(String callback)
 - callback(l, id, t), 鼠标点击Wave Item上. 输入id号, 时间位置, 行数位置.
