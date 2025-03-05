package ru.gb.multithreading_dz4.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.gb.multithreading_dz4.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.gb.multithreading_dz4.di.MyApplication
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    // Внедрение зависимостей
    @Inject
    lateinit var coroutineScope: CoroutineScope
    @Inject
    lateinit var coroutineScope1: CoroutineScope

    private lateinit var binding: ActivityMainBinding
    private val scope = CoroutineScope(Dispatchers.IO)
    private val scope1 = CoroutineScope(Dispatchers.IO)
    private var job: Job? = null
    private var job1: Job? = null
    private var isTimer = true
    private var isTimer1 = false
    private var isTimer2 = false
    private var isTimer3 = 0
    private var minutes1 = 0
    private var minutes2 = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инъекция зависимостей
//        DaggerMainActivityComponent.builder().build().inject(this)

        // Получаем компонент и инжектируем зависимости
        (application as MyApplication).appComponent.inject(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.start.setOnClickListener {
            if (!isTimer1) {
                isTimer1 = true
                isTimer = true
            } else {
                isTimer2 = true
                isTimer = true
            }

            if (isTimer3 == 0) {
                if (isTimer1) {
                    job = scope.launch {
                        createTimerValues1()
                            .map {counter(it, minutes1)}
                            .collect {
                                withContext(Dispatchers.Main) {
                                    binding.timer1.text = it.toString()
                                }
                            }
                    }
                }
            }
            if (isTimer2) {
                job1?.cancel()
                job1 = scope1.launch {
                    createTimerValues2()
                        .map {counter(it, minutes2)}
                        .collect {
                            withContext(Dispatchers.Main) {
                                binding.timer2.text = it.toString()
                            }
                        }
                }
            }
        }

        binding.stop.setOnClickListener {
            isTimer = false
            isTimer3++
        }
    }

    fun createTimerValues1() = flow {
        var speed = 0
        while (true) {
            if (isTimer) {
                Thread.sleep(1000)
                speed++
                if (speed == 60) {
                    minutes1++
                    speed = 0
                }
                if (minutes1 == 60) minutes1 = 0
                emit(speed)
            }
        }
    }

    fun createTimerValues2() = flow {
        var speed2 = 0
        while (true) {
            if (isTimer) {
                Thread.sleep(1000)
                speed2++
                if (speed2 == 60) {
                    minutes2++
                    speed2 = 0
                }
                if (minutes2 == 60) minutes2 = 0
                emit(speed2)
            }
        }
    }

    fun counter(it: Int, minutes: Int): String {
        if (it < 10) {
            if (minutes < 10) {
                return "0" + minutes.toString() + ":0" + it.toString()
            } else {
                return minutes.toString() + ":0" + it.toString()
            }
        } else {
            if (minutes < 10) {
                return "0" + minutes.toString() + ":" + it.toString()
            } else {
                return minutes.toString() + ":" + it.toString()
            }
        }
    }
}