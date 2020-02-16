package OS;

import java.io.Serializable;
import java.util.Arrays;

public class Process implements Cloneable,Serializable {
    int id;
    int block;//块数
    int[] stack;
    int[] page;//页面访问顺序
    int[] physicalblock_num;//块号
    int[] page_table;//页表
    int priority; //优先权
    int cputime; //已占用的cpu时间片
    int alltime; //还需要的时间
    int startblock; //运行多少个时间片之后开始进入阻塞队列,-1代表不会进入阻塞队列
    int blocktime;  //阻塞的时间
    int state;   // 0为就绪,1为阻塞,2为完成

    @Override
    public Process clone() throws CloneNotSupportedException {
        Process process=(Process)super.clone();
        process.setStack(this.stack.clone());
        process.setPage(this.page.clone());
        process.setPage_table(this.page_table.clone());
        process.setPhysicalblock_num(this.physicalblock_num.clone());
        return process;
    }

    public Process(int id, int block, int priority, int cputime, int alltime, int startblock, int blocktime, int state) {
        this.id = id;
        this.block = block;
        this.priority = priority;
        this.cputime = cputime;
        this.alltime = alltime;
        this.startblock = startblock;
        this.blocktime = blocktime;
        this.state = state;
        this.stack=new int[block];
        this.page=new int[10];
        this.physicalblock_num=new int[block];
        this.page_table=new int[10];
        Arrays.fill(stack, -1);
        Arrays.fill(page_table,-1);
        /*final long l = System.currentTimeMillis();
        Arrays.fill(page,(int)( l % 10));*/
        this.page=Ran.testC(10);
    }

    public Process() {

    }


    public String toString2() {
        return "{" +
                "进程" + id +
                ": 分配到的内存块数:" + block +
                //", \n stack=" + Arrays.toString(stack) +
                ", 页面访问序列为:" + Arrays.toString(page) +
                ", 分配到的物理块号:" + Arrays.toString(physicalblock_num) +
                //",\n page_table=" + Arrays.toString(page_table) +
                ", 优先级:" + priority +
                ", 已运行时间:" + cputime +
                ", 还需时间:" + alltime +
                ", 阻塞开始时间:" + startblock +
                ", 阻塞时间:" + blocktime +
                ", 状态:" + state +
                " }\n";
    }
    @Override
    public String toString() {
        return "{" +
                "进程" + id +
                //": 分配到的内存块数:" + block +
                //", \n stack=" + Arrays.toString(stack) +
                //", page=" + Arrays.toString(page) +
                //", 分配到的物理块号:" + Arrays.toString(physicalblock_num) +
                //",\n page_table=" + Arrays.toString(page_table) +
                ": 优先级:" + priority +
                ", 已运行时间:" + cputime +
                ", 还需时间:" + alltime +
                ", 阻塞开始时间:" + startblock +
                ", 阻塞时间:" + blocktime +
                ", 状态:" + state +
                " }\n";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int[] getStack() {
        return stack;
    }

    public void setStack(int[] stack) {
        this.stack = stack;
    }

    public int[] getPage() {
        return page;
    }

    public void setPage(int[] page) {
        this.page = page;
    }

    public int[] getPhysicalblock_num() {
        return physicalblock_num;
    }

    public void setPhysicalblock_num(int[] physicalblock_num) {
        this.physicalblock_num = physicalblock_num;
    }

    public int[] getPage_table() {
        return page_table;
    }

    public void setPage_table(int[] page_table) {
        this.page_table = page_table;
    }

    public int getBlock() {
        return block;
    }

    public void setBlock(int block) {
        this.block = block;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getCputime() {
        return cputime;
    }

    public void setCputime(int cputime) {
        this.cputime = cputime;
    }

    public int getAlltime() {
        return alltime;
    }

    public void setAlltime(int alltime) {
        this.alltime = alltime;
    }

    public int getStartblock() {
        return startblock;
    }

    public void setStartblock(int startblock) {
        this.startblock = startblock;
    }

    public int getBlocktime() {
        return blocktime;
    }

    public void setBlocktime(int blocktime) {
        this.blocktime = blocktime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
