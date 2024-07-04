package javaProTask1;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static java.lang.String.format;

public class TestRunner {


    public static void runTests(Class c) throws InvocationTargetException, IllegalAccessException {
        Map<Method, Integer> methodPriors = new HashMap<>();

        c.getClassLoader();
        Method mBeforeSuite = null;
        Method mAfterSuite = null;
        Method mAfterTest = null;
        Method mBeforeTest = null;
// Проверки методов + сортировка
        int countBeforeSuite = 0;
        int coountAfterSuite = 0;

        List<Method> sortMethods = new ArrayList<>();
        List<Annotation> methAnnot ;
        for (Method method : c.getDeclaredMethods() )  {
            if ( method.isAnnotationPresent(AfterSuite.class))  {
                mAfterSuite = method;
                coountAfterSuite++;
                if (coountAfterSuite>1) {
                    throw new RuntimeException("В классе количество больше одного методов с аанотацией AfterSuite");
                }
            }
            else if ( method.isAnnotationPresent(BeforeSuite.class))  {
                mBeforeSuite = method;
                countBeforeSuite ++;
                if (countBeforeSuite>1) {
                    throw new RuntimeException("В классе количество больше одного методов с аанотацией BeforeSuite");
                }
            }
            else if ( method.isAnnotationPresent(AfterTest.class))  {
                mAfterTest = method;
            }
            else if ( method.isAnnotationPresent(BeforeTest.class))  {
                mBeforeTest = method;

            }
            else if ( method.isAnnotationPresent(Test.class)){
                methodPriors.put(method, method.getAnnotation(Test.class).value());

                if(sortMethods.isEmpty())    {
                sortMethods.add(method);
                }
                else    {// ортировка по убываню
                    boolean found =false;
                    for(int ii=0; ii < sortMethods.size() ; ii++) {
                        int priorNew = methodPriors.get(method);
                        int priorOld = methodPriors.get(sortMethods.get(ii));
                        if (priorNew > priorOld) {
                            found = true;
                            if (ii==sortMethods.size()-1)   {
                                Method methTmp = sortMethods.get(ii);
                                sortMethods.add(methTmp);
                            }
                            else {
                                sortMethods.add(sortMethods.get(sortMethods.size()-1));
                                for(int jj=sortMethods.size()-1;jj>ii;ii--)   {
                                    sortMethods.set(jj,sortMethods.get(jj-1));
                                }
                            }
                            sortMethods.set(ii,method);
                        }
                    }
                    if(!found)  {
                        sortMethods.add(method);
                    }
                }
            }
        }
        Object[] args = null;
        if (mBeforeSuite != null) {
            mBeforeSuite.invoke(c, args);
        }
        for (Method  meth : sortMethods)    {
            if(meth.isAnnotationPresent(CsvSource.class)) {
                String argsS = meth.getAnnotation(CsvSource.class).value();
                args = argsS.split(",");
            }
            if( mBeforeTest != null) {
                mBeforeTest.invoke(c, args);
            }

                meth.invoke(c, args);
            if(mAfterTest != null) {
                mAfterTest.invoke(c, args);
            }
        }
        if( mAfterSuite != null) {
            mAfterSuite.invoke(c, args);
        }
    }
}
