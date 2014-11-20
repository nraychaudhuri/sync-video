package fp;

import akka.dispatch.Mapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class LambdaExample {

    @FunctionalInterface
    interface Mapper<A, B> {
       public B transform(A a);

       public default boolean foo() { return true; }
    }

    public static void main(String[] args) {
//        Function<T,R>  -  function from T to R
        Function<Integer, String> toString = i -> String.valueOf(i);
        List<String> ys = map(Arrays.asList(1, 2, 4), x -> String.valueOf(x));

//        Predicate<T> - function from T to boolean
        Arrays.asList(1, 2, 3, 4).stream().filter(LambdaExample::isEven);
//        Consumer<T> - function from T to void
        Arrays.asList(1, 2, 3, 4).stream().forEach(x -> {});
//        Supplier<T> - function that doesn’t take any input and returns a type T\
        Supplier<LambdaExample> s = LambdaExample::new;


    }


    private static boolean isEven(Integer i) {
        return i % 2 == 0;
    }


    private static <A, B> List<B> map(List<A> xs, Mapper<A, B> mapper) {
       List<B> newList = new ArrayList<>();

       for(A x:xs) {
          newList.add(mapper.transform(x));
       }
       return newList;
    }
}
