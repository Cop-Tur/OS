package OS;

import java.io.*;
import java.util.*;

public class Produce {
    static Process p0=new Process(0,5,12,0,10,3,4,0);
    static Process p1=new Process(1,6,29,0,10,-1,0,0);
    static Process p2=new Process(2,4,27,0,10,-1,0,0);
    static Process p3=new Process(3,5,16,0,10,-1,0,0);
    static Process p4=new Process(4,5,3,0,10,-1,0,0);
    static ArrayList<Process> ready_queue=new ArrayList<>();
    static ArrayList<Process> block_queue=new ArrayList<>();
    static ArrayList<Process> finish_queue=new ArrayList<>();

    static ArrayList<Process>[] readys=new ArrayList[51];
    static ArrayList<Process>[] blocks=new ArrayList[51];
    static ArrayList<Process>[] finishes=new ArrayList[51];
    static Process[] timeProcess=new Process[51];

    static  int time=0;
    static int[] physicalblock =new int[24];

    static void init(){
        physicalblock=Ran.testC(50);
        System.arraycopy(physicalblock, 0, p0.physicalblock_num, 0, p0.getBlock());
        System.arraycopy(physicalblock, p0.getBlock(), p1.physicalblock_num, 0, p1.getBlock());
        System.arraycopy(physicalblock, p0.getBlock()+p1.getBlock(), p2.physicalblock_num, 0, p2.getBlock());
        System.arraycopy(physicalblock, p0.getBlock()+p1.getBlock()+p2.getBlock(), p3.physicalblock_num, 0, p3.getBlock());
        System.arraycopy(physicalblock, p0.getBlock()+p1.getBlock()+p2.getBlock()+p3.getBlock(), p4.physicalblock_num, 0, p4.getBlock());
        ready_queue.add(p0);ready_queue.add(p1);ready_queue.add(p2);ready_queue.add(p3);ready_queue.add(p4);
    }

    static void display_queue() {
        //就绪队列...
        System.out.println("就绪队列："+ready_queue.toString());
        //阻塞队列...
        System.out.println("阻塞队列："+block_queue.toString());
    }

    static void display_stack(Process p) {//存储管理结果显示
        //进程""的LRU栈的变化为（-1代表空闲）
        //输出stack
        System.out.println("LRU栈的变化为："+ Arrays.toString(p.stack));
    }

    static void display_PageTable(Process p, int pageNo) {//展示页面
        //"进程" "该时间片需要访问的页号是"
        //"进程" "在内存中分配到的固定物理块号为"
        //"进程" "的页表为"
        System.out.println("访问该进程的页面"+pageNo);
        for (int i = 0; i < p.getBlock(); i++) {
            if (p.stack[i] == -1) {//有空的物理块
                if (p.page_table[pageNo] != -1) { //有空的物理块但是该页面已经占有一个物理块
                    break;
                } else{
                    p.page_table[pageNo] = p.physicalblock_num[i];//有空的物理块但是该页面没有占有物理块

                }
                break;
            }
            if (i == p.getBlock() - 1 && p.stack[i] != -1) {//没有空的物理块
                if (p.page_table[pageNo] != -1) //没有空的物理块但是该页面已经占有一个物理块
                    break;
                else {//没有空的物理块但是该页面没有占有一个物理块
                    p.page_table[pageNo] = p.page_table[p.stack[0]];
                    p.page_table[p.stack[0]] = -1;
                }
            }
            //!!!!!!输出页表
        }
        System.out.println("页表为:"+ Arrays.toString(p.page_table));
    }

    static void PagePro(Process p) {
        int page_temp=0;//要访问的页面
        for (int i = 0; i < 10; i++) {
            if (p.page[i] != -1) {//-1代表该页面访问过了
                page_temp = p.page[i];
                p.page[i] = -1;
                break;
            }
        }

        display_PageTable(p, page_temp);
        //存储进程
        int flag1 = 0, temp = 0;
        for (int i = 0; i < p.getBlock(); i++) {
            if (page_temp == p.stack[i]) {
                flag1 = 1;
                temp = i;//temp指内存块中存在这个页面的位置
                break;
            }
        }

        if (flag1 == 0) {//内存块中没找到该页
            for (int i = 0; i < p.getBlock(); i++) {
                if (p.stack[i] == -1) {//没找到该页并且有空的物理块
                    p.stack[i] = page_temp;
                    break;
                }
                if (i == p.getBlock() - 1 && p.stack[i] != -1) {//没找到该页并且没有空的物理块
                    if (p.getBlock() - 1 >= 0) System.arraycopy(p.stack, 1, p.stack, 0, p.getBlock() - 1);
                    p.stack[p.getBlock() - 1] = page_temp;
                }
            }
        }
        if (flag1 == 1) {//内存找到该页
            for (int i = 0; i < p.getBlock(); i++) {
                if (p.stack[i] == -1) {//找到该页并且有空的物理块
                    for (int j = temp + 1; j < p.getBlock(); j++) {
                        p.stack[i - 1] = p.stack[i];
                    }
                    for (int k = 0; k < p.getBlock(); k++) {
                        if (p.stack[k] == -1) {
                            p.stack[k] = page_temp;
                            break;
                        }
                    }
                    break;
                }
                if (i == p.getBlock() - 1 && p.stack[i] != -1) {//找到该页并且没有空的物理块
                    if (p.getBlock() - temp + 1 >= 0)
                        System.arraycopy(p.stack, temp + 1, p.stack, temp + 1 - 1, p.getBlock() - temp + 1);
                    p.stack[p.getBlock() - 1] = page_temp;
                }
            }
        }

        display_stack(p);
    }

    static void run() throws CloneNotSupportedException, IOException, ClassNotFoundException {
        Process p=new Process(1,1,1,1,1,1,1,1);

        while (!(ready_queue.isEmpty() && block_queue.isEmpty()))
        {
            time++;
            ready_queue.sort(new Sort());
            System.out.println("时间片"+time);
            if (!ready_queue.isEmpty()) {
                p= ready_queue.get(0);
                timeProcess[time]=p.clone();
                readys[time-1]=Copy.deepCopy(ready_queue);
                blocks[time-1]=Copy.deepCopy(block_queue);
                finishes[time-1]=Copy.deepCopy(finish_queue);

                System.out.println("对编号"+p.getId()+"的进程操作");
                PagePro(p);//存储管理
                p.cputime++;
                p.alltime--;
                p.priority -= 3;
                if (p.startblock > 0)
                    p.startblock--;
                if (p.alltime == 0) {
                    p.state = 2;
                    finish_queue.add(p);  //就绪转结束队列
                    ready_queue.remove(0);
                }

                if (!ready_queue.isEmpty() ) {
                    p = ready_queue.get(0);
                    if (p.startblock == 0) {
                        p.state = 1;
                        block_queue.add(p);
                        ready_queue.remove(0);
                    }
                }
            }
            if (!block_queue.isEmpty()) {
                for (int i = 0; i < block_queue.size(); i++)
                {
                    p=block_queue.get(i);
                    if (p.blocktime > 0)
                        p.blocktime--;
                    Iterator<Process> iterator = block_queue.iterator();
                    while(iterator.hasNext()) {
                        Process process = iterator.next();
                        if(process.getBlocktime()==0) {
                            process.setState(0);
                            ready_queue.add(process);
                            ready_queue.sort(new Sort());
                            iterator.remove();
                        }
                    }
                }
            }
            System.out.print("进程信息:"+p.toString2());
            System.out.println("队列信息:");
            display_queue();
            //System.out.println("!!!!!!!!"+readys[0].get(0).toString2());
            System.out.println("\n\n");

        }
        System.out.println("就绪队列运行结束");
        readys[50]=new ArrayList<>();
        blocks[50]=new ArrayList<>();
    }

    static void init_display() {
        System.out.println("Init:");
        System.out.print(p0.toString2());
        System.out.print(p1.toString2());
        System.out.print(p2.toString2());
        System.out.print(p3.toString2());
        System.out.println(p4.toString2());
    }

    public static void main(String[] args) throws Exception {
        init();
        init_display();
        run();
        Scanner sc = new Scanner(System.in);
        System.out.println("输入想指定查看的时间片号或按q结束");
        String times=sc.nextLine();
        while (!times.matches("q")){
            System.out.println("对编号"+timeProcess[Integer.parseInt(times)].getId()+"的进程操作");
            PagePro(timeProcess[Integer.parseInt(times)]);
            System.out.print("进程信息:"+timeProcess[Integer.parseInt(times)].toString2());
            System.out.println("队列信息:");
            System.out.println("就绪队列："+readys[Integer.parseInt(times)].toString());
            System.out.println("阻塞队列："+blocks[Integer.parseInt(times)].toString());
            times=sc.nextLine();
        }
    }
}
