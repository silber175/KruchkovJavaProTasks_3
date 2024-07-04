package javaProTask1;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.*;

@ToString
@EqualsAndHashCode
public class Account {
        private String name;
        private final Map<Currency, Integer> accCurrCount= new HashMap<>();
        // private ArrayList<AccSaver> accSavers;
        private Deque<Command> saves = new ArrayDeque<>();

        private String acntType;     // Для дальнейшего развития счета добавлен тип

        public Account(String vName) {
            if (vName == null)
                throw new IllegalArgumentException("Имя не может быть null ");
            if (vName.isBlank() )
                throw new IllegalArgumentException("Имя не может быть пустым ");
            this.name = vName;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String vName) {
            if (vName == null)
                throw new IllegalArgumentException("Имя не может быть null ");
            if (vName.isBlank() )
                throw new IllegalArgumentException("Имя не может быть пустым ");
            String tmp = Account.this.name;
            saves.push(()->Account.this.name=tmp);
            this.name = vName;
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
                saves.push(()->Account.this.accCurrCount.remove(vCurr));
                this.accCurrCount.put(vCurr, vCount);
            }
            else    {
                if (this.accCurrCount.containsKey(vCurr)) {
                    int tmp = Account.this.accCurrCount.get(vCurr);
                    saves.push(()->Account.this.accCurrCount.replace(vCurr, tmp));
                    this.accCurrCount.replace(vCurr, vCount);
                } else {
                    //  еще не было добавляемой  валюты , то команда по удалению
                    // добавляемой валюты
                    saves.push(()->Account.this.accCurrCount.remove(vCurr));
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

        public AccSaver  save(String savePointName) {
            return (new AccSave(savePointName));
        }

        // Откат на сохраненное состояние с именем aSaveName ля тестирования
        public void undo()  {
            if (saves == null | saves.size() == 0)  {
                throw new IllegalArgumentException( "Ошибка : нет данных для undo ");
            }
            saves.pop().make();
        }
        public void restore(AccSaver saver) {
            saver.restore();
        }
        private class AccSave implements AccSaver {
            public String savePointName;
            private String name = Account.this.name;
            private Map<Currency, Integer> accCurrCount ;

            public AccSave(String savePointName)    {
                this.savePointName = savePointName;
                this.name = name;
                this.accCurrCount = new HashMap<>(Account.this.accCurrCount);
            }

            public void restore()   {
                Account.this.name = name;
                Account.this.accCurrCount.clear();
                Account.this.accCurrCount.putAll(accCurrCount);
            }
        }


    }


