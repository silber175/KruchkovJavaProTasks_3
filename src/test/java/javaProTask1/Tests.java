
package javaProTask1;


import javaProTask1.*;

import java.util.*;

public class Tests {
    @BeforeSuite



// Account creating with wrong nam
    public void accCreate() {
        try {
            Account vAccount = new Account("Иванов Иван Иванович");
            if(!("Иванов Иван Иванович".equals(vAccount.getName())))  {
                throw new RuntimeException("Account  создается с неверным именем " + vAccount.getName() + " вместо "+"Иванов Иван Иванович");
            }
        }
        catch (Exception e)   {
            throw new RuntimeException("Ошибка в тесте или классе : Account  создается с неверным именем "+e.getLocalizedMessage());
        }
    }
    // Account метод undo не проверяет, что для отката нет данны
    @Test(10)
    public void AccEmptyCreate()    {

        Account vAccount = new Account("Петров Леон");

        try {
             vAccount.undo();
        }
        
        catch (Exception e) {
            return;
        }
    
       throw new RuntimeException(" Account метод undo не проверяет, что для отката нет данных ");
       
    }
    // Account on creating don't check for empty or null
    @Test
    public void accNullCreate()    {

        try {

            
                     Account vAccount = new Account(null);
        }
        catch (Exception e) {
                return;
        }
        throw new RuntimeException("Account при создании не проверяется null  значение name");
    }
    @Test
    public void acEmptyCreate()    {

        try {
                  Account vAccount = new Account("");
        }
        catch (Exception e)     {   
            return;
        }
        throw new RuntimeException(" Account при создании не проверяется null или пустое значение name ");
        
    }
    @Test
    // Не записывается валюта  либо количество
    public void CcurrencyCreate()    {
        Account vAccount = new Account("Иванов Петр Иванович");
        try {
            Currency vUsd = Currency.getInstance("USD");
            Currency vEur = Currency.getInstance("EUR");

            vAccount.changeBal(vUsd, 2000);
            vAccount.changeBal(vEur, 1000);
            Map<Currency, Integer> vCurrCount = vAccount.getAccCurrCount();

            if(!(vCurrCount.get(vUsd).equals(2000) && (vCurrCount.get(vEur).equals(1000) )))    {
                throw new RuntimeException("Не записывается валюта 1 либо количество ");
            }
           
        }
        catch (Exception e)   {
            throw new RuntimeException("Ошибка в тесте или классе : Account Не записывается валюта  либо количество "+e.getLocalizedMessage());
        }
    }
    @Test
    // Getter  валюты позволяет менять суммы
    public void CurrencyGetCheck()    {
        Account vAccount = new Account("Иванов Петр Иванович");
        Currency vUsd = Currency.getInstance("USD");
        vAccount.changeBal(vUsd,2000);
        try {
            Map<Currency, Integer> vCurrCount = vAccount.getAccCurrCount();
            vCurrCount.replace(vUsd, 100);
            vCurrCount = vAccount.getAccCurrCount();

            if(!(vCurrCount.get(vUsd).equals(2000))) {
                throw new RuntimeException("Account Getter  валюты позволяет менять суммы");   
            }
        }
        catch (Exception e)   {
            throw new RuntimeException("Ошибка в тесте или классе : Account Getter  валюты позволяет менять суммы "+e.getLocalizedMessage());
        }
    }
    @Test
    // валюта не проверяется по списку допустимых значений
    public void currInValidCreate() {
        boolean thrown = false;
        Account vAccount = new Account("Иванов Петр Иванович");

        try {
             vAccount.changeBal(null,1000);
        }
        catch (Exception e)   {
            return;            
        }
        throw new RuntimeException("Account : валюта не проверяется по списку допустимых значений ");
    }
    @Test
    // Количество валюты не проверяется на отрицательное значение
    public void CurrNegotCreate() {
        boolean thrown = false;
        Account vAccount = new Account("Иванов Петр Иванович");
        Currency vEur= Currency.getInstance("EUR");

        try {
            vAccount.changeBal(vEur,-1000);
        }
        catch (Exception e)   {
            return;
        }
        throw new RuntimeException("Ошибка в тесте или классе : Account : Количество валюты не проверяется на отрицательное значение ");
    }

    @Test
    // Проверка работы undo
    public void undoNWorkFrame()    {
        Account vAccount = new Account("Кузнецов Николай Петрович");
        Currency vRur= Currency.getInstance("RUR");
        vAccount.changeBal(vRur,100);
        vAccount.setName("Василий Иванов");
        vAccount.changeBal(vRur,10);
       
            vAccount.undo();
            Map<Currency, Integer> vCurrCount = vAccount.getAccCurrCount();
            if(!(vCurrCount.get(vRur).equals(100))) {
                throw new RuntimeException("Error test : метод undo неверно откатывает 1 раз колчество валюты");
            }
            if (vCurrCount.get(vRur)==null) {
                throw new RuntimeException("Error test : метод undo при откате колчества валюты , валюта удаляется из объекта");
            }

            vAccount.undo();
            if(!(vAccount.getName().equals("Кузнецов Николай Петрович")))   {
                throw new RuntimeException("Error test : метод undo  неверно откатывает наименование");
            }

            vAccount.undo();
            vCurrCount = vAccount.getAccCurrCount();
            if(vCurrCount.get(vRur).equals(null))    {
                throw new RuntimeException("Error test : метод undo при откате  валюты , валюта не пропадает из объекта ");
            }
    }

    @Test
    @CsvSource("Сидоров И.К, USD, 2000, EUR, 1000, false")
    // Проверка метода сохранения
    public void savePointCheck(String name, String curr1, int sum1, String curr2, int sum2, boolean ignore )  {
        Manager accSavers = new Manager();

            Account vAccount = new Account(name /*"Сидоров И.К"*/ );
            Currency vUsd = Currency.getInstance(curr1 /* "USD" */);
            Currency vEur = Currency.getInstance( curr2 /* "EUR" */ );
        if (!ignore) {
            vAccount.changeBal(vUsd, sum1);
            vAccount.changeBal(vEur, sum2);
        }
        vAccount.changeBal(vEur,1500);

            String aSaveName1 ="sp1";
            AccSaver vAccSaver =  vAccount.save( aSaveName1);
            accSavers.saves = new HashMap<>();
            accSavers.saves.put(aSaveName1, vAccSaver);

            vAccount.setName("Иван Васильевич");
            vAccount.changeBal(vUsd,6000);
            vAccount.changeBal(vEur,3000);

            String aSaveName2 = "sp2";
            vAccSaver =  vAccount.save( aSaveName2);
            accSavers.saves.put(aSaveName2, vAccSaver);

            vAccount.setName("Pol Robson");
            vAccount.restore(accSavers.saves.get(aSaveName1));
            if(!( vAccount.getName().equals("Сидоров И.К")))    {
                throw new RuntimeException("Error test : метод save  неверно сохраняет наименование");
            }

            Map<Currency, Integer> vCurrCount = vAccount.getAccCurrCount();
            if (vCurrCount.get(vUsd).equals(null)) {
                throw new RuntimeException("Error test : метод save не сохраняет валюту 1 ");
            }
            if (vCurrCount.get(vEur).equals(null))  {
                throw new RuntimeException("Error test : метод save не сохраняет валюту 2 ");
            }
            if(!(vCurrCount.get(vEur).equals(1500)))    {
                throw new RuntimeException("Error test : метод save неверно сохраняет колчество валюты"); 
            } 
            if(!(vCurrCount.get(vUsd).equals(2000)))   {
                throw new RuntimeException("Error test : метод save неверно сохраняет колчество валюты");
            }

            vAccount.restore(accSavers.saves.get(aSaveName2));
            if(!(vAccount.getName().equals("Иван Васильевич"))) {
                throw new RuntimeException("Error test : метод save  неверно сохраняет наименование");
            }

            vCurrCount = vAccount.getAccCurrCount();
            if (  vCurrCount.get(vUsd).equals(null)) {
                throw new RuntimeException("Error test : метод save не сохраняет валюту");
            }
            if (  vCurrCount.get(vEur).equals(null))  {
                throw new RuntimeException("Error test : метод save не сохраняет валюту");
            }
            if(!(vCurrCount.get(vUsd).equals(6000)))   {
                throw new RuntimeException("Error test : метод save неверно сохраняет колчество валюты");
            }
            if(!(vCurrCount.get(vEur).equals(3000))) {
                throw new RuntimeException("Error test : метод save неверно сохраняет колчество валюты");
            }
    }
    // Проверка метода undo при добавлении поля вид счета в класс Account
    // При наличии поля private String acntType  тесты
    @AfterSuite
    public static void extendsCheck()   {
        class BAccount {
            private String name;
            private final Map<Currency, Integer> accCurrCount= new HashMap<>();
            private String acntType;

            BAccount(String vName)  {
                this.name = vName;
            }

            private Deque<Command> saves = new ArrayDeque<>();
            public String getName() {
                return this.name;
            }

            public void setName(String vName) {
                if (vName == null)
                    throw new IllegalArgumentException("Имя не может быть null ");
                if (vName.isBlank() )
                    throw new IllegalArgumentException("Имя не может быть пустым ");
                String tmp = BAccount.this.name;
                saves.push(()->BAccount.this.name=tmp);
                this.name = vName;
            }

            public String  getAccType() {
                return  this.acntType;
            }

            public void setAccType(String vAccType) {
                if (vAccType == null)
                    throw new IllegalArgumentException("vAccType не может быть null ");
                if (vAccType.isBlank() )
                    throw new IllegalArgumentException("vAccType не может быть пустым ");
                String tmp = BAccount.this.acntType;
                saves.push(()->BAccount.this.acntType=tmp);
                this.acntType = vAccType;
            }

            public Map<Currency, Integer> getAccCurrCount() {
                return new HashMap<>(accCurrCount);
            }

            public void changeBal(Currency  vCurr, int vCount) {
                if (vCount < 0)
                    throw new IllegalArgumentException("Количество валюты не может быть отрицательным");
                if (vCurr == null)
                    throw new IllegalArgumentException( "Ошибка : Недопустимая валюта ");
                if (this.accCurrCount.size() == 0)   {
                    //  еще не было никакай валюты до ввода баланса, то команда по удалению
                    // добавляемой валюты
                    saves.push(()->BAccount.this.accCurrCount.remove(vCurr));
                    this.accCurrCount.put(vCurr, vCount);
                }
                else    {
                    if (this.accCurrCount.containsKey(vCurr)) {
                        int tmp = BAccount.this.accCurrCount.get(vCurr);
                        saves.push(()->BAccount.this.accCurrCount.replace(vCurr, tmp));
                        this.accCurrCount.replace(vCurr, vCount);
                    }
                    else {
                        //  еще не было добавляемой  валюты , то команда по удалению
                        // добавляемой валюты
                        saves.push(()->BAccount.this.accCurrCount.remove(vCurr));
                        this.accCurrCount.put(vCurr, vCount);
                    }
                }
            }

            // Метод для тестирования результатов изменений валюты
            public String printAccCurrCount() {
                String sRes = " Остаки валют : ";
                sRes = sRes + this.accCurrCount.toString();
                return sRes;
            }

            // Откат на сохраненное состояние с именем aSaveName ля тестирования
            public void undo()  {
                saves.pop().make();
            }
        }
        // Test undo
        try {
            System.out.println("Тестирование undo после добаления нового поля в класс");
            BAccount vAccount = new BAccount("Кузнецов Николай Петрович");
            vAccount.setAccType("Специальный");
            vAccount.setAccType("Премиальный");
            Currency vRur= Currency.getInstance("RUR");
            vAccount.changeBal(vRur,100);
            vAccount.setName("Василий Иванов");
            vAccount.changeBal(vRur,20);

            // undo begin
            vAccount.undo();
            Map<Currency, Integer> vCurrCount = vAccount.getAccCurrCount();
            if(!(vCurrCount.get(vRur).equals(100)))    {
                throw new RuntimeException("Error test : метод undo неверно откатывает 1 раз колчество валюты");
            }
            if ( vCurrCount.get(vRur).equals(null))   {
                throw new RuntimeException("Error test : метод undo при откате колчества валюты , валюта удаляется из объекта");
            }

            vAccount.undo();
            if(!(vAccount.getName().equals( "Кузнецов Николай Петрович")))    {
                throw new RuntimeException( "Error test : метод undo  неверно откатывает наименование");
            }
            vAccount.undo();
            vCurrCount = vAccount.getAccCurrCount();
            if(!(vCurrCount.get(vRur).equals(null)))    {
                throw new RuntimeException("Error test : метод undo при откате  валюты , валюта не пропадает из объекта");
        }
            vAccount.undo();
            if(!(vAccount.getAccType().equals( "Специальный"))) {
                throw new RuntimeException("Error test : добавление нового поля в класс требует зменения метода undo");
            }
        }
        catch (Exception e)   {
            throw new RuntimeException("Ошибка в тесте или классе : Проверка метода undo при добавлении поля вид счета в класс Account "+e.getLocalizedMessage());
        }
    }



}

