import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    void getName_acc() {
        String test_name="Test_1";
        Account acc_test = new Account(test_name);
        String result_name = acc_test.getName_acc();
        Assert.assertEquals(test_name,result_name);
    }

    @Test
    void getValue() {
        Account acc_test = new Account("Test", Account.Currency.RUR,100);
        HashMap<Account.Currency,Integer> test_value = new HashMap<>();
        test_value.put(Account.Currency.RUR,100);
        Assert.assertEquals(test_value,acc_test.getValue());

    }

    @Test
    void setName_acc() {
        String test_name="Test_1";
        Account acc_test = new Account(test_name);
        String test_name2="NewName";
        acc_test.setName_acc(test_name2);
        Assert.assertEquals(test_name2,acc_test.getName_acc());
        Assert.assertThrows(IllegalArgumentException.class,() ->{acc_test.setName_acc("");});
    }

    @Test
    void setValue() {
        Account acc_test = new Account("Test");
        HashMap<Account.Currency,Integer> test_value = new HashMap<>();
        test_value.put(Account.Currency.RUR,100);
        acc_test.setValue(Account.Currency.RUR,100);
        Assert.assertEquals(test_value,acc_test.getValue());
        Assert.assertThrows(IllegalArgumentException.class,() ->{acc_test.setValue(Account.Currency.RUR,-100);});

    }

    @Test
    void revert() {
        Account acc_test = new Account("Test");
        String test_name="Test_1";
        String test_name2="NewName";
        acc_test.setName_acc(test_name);
        acc_test.setName_acc(test_name2);
        Assert.assertEquals(test_name2,acc_test.getName_acc());
        acc_test.undo();
        Assert.assertEquals(test_name,acc_test.getName_acc());
    }

    @Test
    void shot() {
        Account acc_test = new Account("Test");
        acc_test.setValue(Account.Currency.RUR,100);
        String test_name="Test_1";
        String test_name2="NewName";
        HashMap<Account.Currency,Integer> test_value = new HashMap<>();
        test_value.put(Account.Currency.RUR,400);
        HashMap<Account.Currency,Integer> test_value2 = new HashMap<>();
        test_value2.put(Account.Currency.RUR,500);
        acc_test.setName_acc(test_name);
        acc_test.setValue(Account.Currency.RUR,400);
        Assert.assertEquals(test_name,acc_test.getName_acc());
        Assert.assertEquals(test_value,acc_test.getValue());
        Loadable qs1 = acc_test.Shot();
        acc_test.setName_acc(test_name2);
        acc_test.setValue(Account.Currency.RUR,500);
        Assert.assertEquals(test_name2,acc_test.getName_acc());
        Assert.assertEquals(test_value2,acc_test.getValue());
        qs1.Restore();
        Assert.assertEquals(test_name,acc_test.getName_acc());
        Assert.assertEquals(test_value,acc_test.getValue());
    }
}