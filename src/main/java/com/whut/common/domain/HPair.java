package com.whut.common.domain;
/**
 * Created by 杨赟 on 2018-06-12.
 */
public class HPair<E, F> {
    private E first;
    private F second;
    public HPair(){

    }
    public HPair(E first, F second){
        this.first=first;
        this.second=second;
    }
    public E getFirst() {
        return first;
    }
    public void setFirst(E first) {
        this.first = first;
    }
    public F getSecond() {
        return second;
    }
    public void setSecond(F second) {
        this.second = second;
    }
}
