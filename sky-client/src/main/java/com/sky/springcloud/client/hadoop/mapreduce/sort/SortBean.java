package com.sky.springcloud.client.hadoop.mapreduce.sort;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author 太阿
 * @since 0.1.0
 * @date 2021-02-22
 *
 * 排序的实例化的对象。
 * 只能针对MapReducer里的key做排序？
 */
public class SortBean implements WritableComparable<SortBean>{
    private String word;
    private int num;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "SortBean{" +
                "word='" + word + '\'' +
                ", num=" + num +
                '}';
    }

    /**
     * 比较器，按照我们自己定义的规则进行排序
     * 排序规则： 要求第一列按照字典顺序进行排列，第一列相同的时候, 第二列按照升序进行排列。
     * @param o
     * @return
     */
    @Override
    public int compareTo(SortBean o) {
        //1、先对第一列进行比较
        int result = this.word.compareTo(o.word);

        //2、当第一列相等的时候，再对第二列进行比较
        if (result == 0){
           return this.num - o.num;
        }
        return result;
    }

    /**
     * 实现序列化
     * @param out
     * @throws IOException
     */
    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(word);
        out.writeInt(num);
    }

    /**
     * 实现反序列化
     * @param in
     * @throws IOException
     */
    @Override
    public void readFields(DataInput in) throws IOException {
        this.word = in.readUTF();
        this.num = in.readInt();
    }
}
