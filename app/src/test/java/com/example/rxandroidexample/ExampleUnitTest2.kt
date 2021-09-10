package com.example.rxandroidexample

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.internal.operators.observable.ObservableRange
import org.junit.Test
import java.util.concurrent.TimeUnit

// rx operation
class ExampleUnitTest2 {
    @Test
    fun defer() {
        val justSrc = Observable.just(
            System.currentTimeMillis()
        )

        val deferSrc = Observable.defer {
            Observable.just(System.currentTimeMillis())
        }

        println("#1 current time ${System.currentTimeMillis()}")
        Thread.sleep(5000)
        println("#2 current time ${System.currentTimeMillis()}")
        justSrc.subscribe {
            println("#1 justSrc $it")
        }
        deferSrc.subscribe {
            println("#2 deferSrc $it")
        }
    }

    @Test
    fun emptyAndNever() {
        Observable.empty<String>().doOnTerminate {
            println("empty HI")
        }.subscribe {
            println("#1 it")
        }

        Observable.never<String>().doOnTerminate {
            println("never HI")
        }.subscribe {
            println("#1 it")
        }

        Thread.sleep(1000)
    }

    @Test
    fun range() {
        Observable.range(1, 10).subscribe {
            println("#$it")
        }
        Thread.sleep(1000)
    }

    @Test
    fun timer() {
        val src = Observable.timer(1, TimeUnit.SECONDS)
        println("구독")
        src.subscribe {
            println("실행")
        }
        println("Sleep")
        Thread.sleep(1000)
    }

    @Test
    fun map() {
        val src = Observable.range(1, 6)
        src.map {
            it * 2
        }.subscribe {
            println("# $it")
        }
        Thread.sleep(10000)
    }

    @Test
    fun flatMap() {
        val src = Observable.range(2, 8)
        src.flatMap { x ->
            Observable
                .range(1, 9)
                .map { y ->
                    "$x x $y = ${x * y}"
                }
        }.subscribe {
            println(it)
        }
    }

    @Test
    fun buffer() {
        Observable.range(0, 10)
            .buffer(3)
            .subscribe {
                println("버퍼 데이터 발행")
                it.forEach { item ->
                    println("$item")
                }
            }
    }

    @Test
    fun scan() {
        Observable.just("A", "B", "C", "D", "E")
            .scan { t1, t2 ->
                t1 + t2
            }.subscribe {
                println(it)
            }
    }

    @Test
    fun groupBy() {
        Observable.just("A1", "B1", "B2", "A22", "Hello", "Hi")
            .groupBy {
                when {
                    it.first() == 'A' -> "A Group"
                    it.first() == 'B' -> "B Group"
                    else -> "Other Group"
                }
            }.subscribe { group ->
                println("Group Name: ${group.key}")
                group.subscribe {
                    println("${group.key.padEnd(10, ' ')}: $it")
                }
            }
    }

    /**
     * 특정 시간 동안 다른 아이템이 발행되지 않을 때만 아이템을 발행한다.
     */
    @Test
    fun debounce() {
        Observable.create<String> {
            it.onNext("1") // *
            Thread.sleep(100)
            it.onNext("2")
            it.onNext("3")
            it.onNext("4")
            it.onNext("5") // *
            Thread.sleep(100)
            it.onNext("6") // *
        }.debounce(90, TimeUnit.MILLISECONDS)
            .subscribe {
                println("# $it")
            }

        Thread.sleep(3000)
    }

    /**
     * 중복 제거
     */
    @Test
    fun distinct() {
        Observable.just(1,2,2,1,3)
            .distinct()
            .subscribe {
                println(it)
            }
    }

    @Test
    fun elementAt() {
        Observable.just(1,2,3,4,5)
            .elementAt(3)
            .subscribe {
                println(it)
            }
    }

    @Test
    fun filter() {
        Observable.just(1,2,3,4,5,6)
            .filter {
                it % 2 == 0
            }.subscribe {
                println("$it")
            }
    }

    /**
     * 일정 시간 간격으로 최근에 발행된 아이템을 발행한다.
     */
    @Test
    fun sample() {
        Observable.interval(100, TimeUnit.MILLISECONDS)
            .sample(300, TimeUnit.MILLISECONDS)
            .subscribe {
                println("#$it")
            }
        Thread.sleep(5000)
    }

    /**
     * n개의 아이템을 무시하고 이후에 나오는 아이템을 발행
     */
    @Test
    fun skip() {
        Observable.just(1,2,3,4,5,6)
            .skip(2)
            .subscribe {
                println("$it")
            }
    }

    @Test
    fun take() {
        Observable.just(1, 2, 3, 4, 5, 6)
            .take(2)
            .subscribe {
                println("$it")
            }
    }

    @Test
    fun all() {
        Observable.just(2,4,6,8,10)
            .all {
                it % 2 == 0
            }.subscribe { it ->
                println("$it")
            }
    }

    @Test
    fun amb() {
        val list = arrayListOf(
            Observable.just(1,2,3),
            Observable.just(2,2,2),
            Observable.just(4,4,4),
            Observable.just(9,8,7),
        )
        Observable.amb(list)
            .subscribe {
                println("$it")
            }
    }
}