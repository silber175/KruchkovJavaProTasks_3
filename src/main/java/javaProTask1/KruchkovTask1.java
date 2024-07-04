package javaProTask1;

import java.util.Currency;
import java.util.HashMap;

public class KruchkovTask1 {
        public static void main(String[] args)

    {
        Manager accSavers = new Manager();

        System.out.println("Task  call example");
        // Simple test
        Account vAccount = new Account("Сидоров И.К");
        Currency vUsd = Currency.getInstance("USD");
        Currency vEur= Currency.getInstance("EUR");


        vAccount.changeBal(vUsd,2000);
        vAccount.changeBal(vEur,1000);
        String vRes = vAccount.printAccCurrCount() ;
        System.out.println("Before save point 1 "+vRes);
        System.out.println(vAccount.getName());

        vAccount.changeBal(vEur,1500);
        vAccount.setName("Иван Васильевич");
        AccSaver vAccSaver = vAccount.save("sp1");
        accSavers.saves = new HashMap<String , AccSaver>();
        accSavers.saves.put("sp1", vAccSaver);
        System.out.println("Call save point 1");

        vRes = vAccount.printAccCurrCount() ;
        System.out.println("After change"+vRes);
        System.out.println(vAccount.getName());

        vAccount.changeBal(vEur,3000);
        vRes = vAccount.printAccCurrCount() ;
        System.out.println("Before save point 2"+vRes);
        System.out.println(vAccount.getName());
        vAccSaver = vAccount.save("sp2");

        accSavers.saves.put("sp2", vAccSaver);
        System.out.println("Call save point 2");

        vAccount.undo();
        vRes = vAccount.printAccCurrCount() ;
        System.out.println("1 undo "+vRes);
        System.out.println(vAccount.getName());

        vAccount.undo();
        vRes = vAccount.printAccCurrCount() ;
        System.out.println("2 undo "+vRes);
        System.out.println(vAccount.getName());

        vAccount.setName("Pol Robson");

        System.out.println("Before restore save points"+vRes);
        System.out.println(vAccount.getName());

        vAccount.restore(accSavers.saves.get("sp2"));
        System.out.println("Call restore save point 2");
        vRes = vAccount.printAccCurrCount() ;
        System.out.println("After restore save point 2"+vRes);
        System.out.println(vAccount.getName());

        vAccount.restore(accSavers.saves.get("sp1"));
        System.out.println("Call restore save point 1");
        vRes = vAccount.printAccCurrCount() ;
        System.out.println("After restore save point 1"+vRes);
        System.out.println(vAccount.getName());

    }
};

