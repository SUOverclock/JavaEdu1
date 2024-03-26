import jdk.jshell.spi.ExecutionControl;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

interface Undo{
    public void step();
}

interface Loadable {
    void Restore();
}
class NoToUndo extends RuntimeException {}

public class Account {
    private String name_acc;
    public enum Currency {RUR,EUR,USD};
    private HashMap<Currency,Integer> value = new HashMap<>();
    private Deque<Undo> undoTable = new ArrayDeque<>();

    private class Snapshot implements Loadable{
        private String name_acc;
        private HashMap<Account.Currency,Integer> value = new HashMap<>();

        public Snapshot() {
            this.name_acc = Account.this.name_acc;
            this.value= new HashMap<>(Account.this.value);
        }

        @Override
        public void Restore() {
            Account.this.name_acc = this.name_acc;
            Account.this.value= new HashMap<>(this.value);
        }
    }

    //**************************Constructors***************************************************************************
    public Account(String name_acc, Currency cur, Integer value) {
        setName_acc(name_acc);
        setValue(cur,value);
    }

    public Account(String name_acc) {
        setName_acc(name_acc);
        setValue(Currency.RUR,0);
    }
    //**************************GET/SET***************************************************************************
    public String getName_acc() {
        return name_acc;
    }

    public HashMap<Currency, Integer> getValue() {
        return new HashMap<Currency, Integer>(this.value);
    }

    public void setName_acc(String name_acc) {
        if(name_acc==null || name_acc.isEmpty()) throw new IllegalArgumentException();
        if(this.name_acc!=null) { // call not from constructor
        String tmp = this.name_acc;
        this.undoTable.push(()-> {this.name_acc=tmp;}); }
        this.name_acc = name_acc;
    }

    public void setValue(Currency cur, Integer value) {
        if(value<0) throw new IllegalArgumentException(); // call from constructor
        if(this.value.isEmpty()) this.value.put(cur,value);
        else {
        if(this.value.containsKey(cur)) {
            int tmp = this.value.get(cur);
            this.undoTable.push(()->this.value.put(cur,tmp));
        }
        else this.undoTable.push(()->this.value.remove(cur));
        this.value.put(cur,value); }
    }
    //**************************UNDO***************************************************************************
    public Account undo() {
        if(undoTable.isEmpty()) throw new NoToUndo();
        undoTable.pop().step();
        return this;
    }
    //**************************SNAPSHOT***********************************************************************
    public Loadable Shot() {return new Snapshot();}

}

