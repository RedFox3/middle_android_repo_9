package ru.yandex.loginapp

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    private var viewModel: LoginViewModel? = null
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        viewModel = LoginViewModel()
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        viewModel = null
        Dispatchers.resetMain()
    }

    @Test
    fun `login with empty fields sets EmptyFieldsError`() {
        viewModel?.login("", "")
        assertEquals(viewModel?.state?.value, LoginScreenState.EmptyFieldsError)
    }

    @Test
    fun `login with invalid email sets EmailValidationError`() {
        viewModel?.login("testEmail", "testPassword")
        assertEquals(viewModel?.state?.value, LoginScreenState.EmailValidationError)
    }

    @Test
    fun `login with valid data sets Loading`() = runTest {
        launch {
            viewModel?.login("practicum@yandex.ru", "testPassword")
        }

        testDispatcher.scheduler.runCurrent()

        assertEquals(viewModel?.state?.value, LoginScreenState.Loading)
    }

    @Test
    fun `login with valid data sets Loading then Success`() = runTest {
        launch {
            viewModel?.login("practicum@yandex.ru", "testPassword")
        }

        testDispatcher.scheduler.runCurrent()

        assertEquals(viewModel?.state?.value, LoginScreenState.Loading)

        testDispatcher.scheduler.advanceTimeBy(3000)
        testDispatcher.scheduler.runCurrent()

        assertEquals(viewModel?.state?.value, LoginScreenState.Success)
    }
}