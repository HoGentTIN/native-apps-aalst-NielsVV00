package com.example.project3pt

import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.example.project3pt.database.WedstrijdDao
import com.example.project3pt.models.MijnWedstrijden
import com.example.project3pt.models.Wedstrijd
import com.example.project3pt.repositories.UserRepository
import com.example.project3pt.repositories.WedstrijdRepository
import com.example.project3pt.services.WedstrijdService
import com.google.gson.Gson
import io.mockk.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class WedstrijdRepositoryTest{
    private val wedstrijdDao: WedstrijdDao = mockk()
    private lateinit var repository: WedstrijdRepository

    private val wed1: Wedstrijd = mockk()
    private val wed2: Wedstrijd = mockk()
    private val wed3: Wedstrijd = mockk()
    private val userRepository: UserRepository = mockk()
    private val wedstrijdService: WedstrijdService = mockk()
    private val sharedPreferences: SharedPreferences = mockk()
    private val connectivityManager: ConnectivityManager = mockk()
    private val networkInfo: NetworkInfo = mockk()

    @Before
    fun setUp() {
        coEvery { wedstrijdDao.getAll() } returns listOf(wed2, wed3)
        coEvery { wedstrijdDao.insert(any()) } returns Unit
        coEvery { wedstrijdDao.nukeTable() } returns Unit

        coEvery { wedstrijdService.getWedstrijden() } returns arrayListOf(wed1, wed2)

        every { connectivityManager.activeNetworkInfo } returns networkInfo
        repository = WedstrijdRepository(userRepository, sharedPreferences, connectivityManager, wedstrijdDao, wedstrijdService)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun wedstrijdRepository_getAll_online_returnsWedstrijden() {
        // Arrange
        every { networkInfo.isConnected } returns true
        // Act
        runBlockingTest {
            val wedstrijden = repository.getAll()
            // Assert
            coVerify { wedstrijdService.getWedstrijden() }
            coVerify { wedstrijdDao.nukeTable() }
            Assert.assertEquals(2, wedstrijden.size)
            Assert.assertTrue(wedstrijden.contains(wed1))
            Assert.assertTrue(wedstrijden.contains(wed2))
            Assert.assertFalse(wedstrijden.contains(wed3))
        }
    }

    @Test
    fun wedstrijdRepository_getAll_offline_returnsDaoWedstrijden() {
        // Arrange
        every { networkInfo.isConnected } returns false
        // Act
        runBlockingTest {
            val wedstrijden = repository.getAll()
            // Assert
            coVerify { wedstrijdService.getWedstrijden() wasNot Called }
            Assert.assertEquals(2, wedstrijden.size)
            Assert.assertFalse(wedstrijden.contains(wed1))
            Assert.assertTrue(wedstrijden.contains(wed2))
            Assert.assertTrue(wedstrijden.contains(wed3))
        }
    }

    @Test
    fun wedstrijdRepository_getMijnWedstrijden_works() {
        // Arrange
        coEvery { sharedPreferences.getString("mijnWedstrijden", null) } returns Gson().toJson(
            MijnWedstrijden(listOf(wed1, wed3))
        )
        // Act
        runBlockingTest {
            val wedstrijden = repository.getMijnWedstrijden()
            // Assert
            coVerify { userRepository.getMijnWedstrijden() wasNot Called }
            Assert.assertEquals(2, wedstrijden!!.size)
            Assert.assertTrue(wedstrijden.contains(wed1))
            Assert.assertFalse(wedstrijden.contains(wed2))
            Assert.assertTrue(wedstrijden.contains(wed3))
        }
    }

        @Test
        fun wedstrijdRepository_getMijnWedstrijdenIfEmpty_works() {
            // Arrange
            val editor: SharedPreferences.Editor = mockk()
            every { sharedPreferences.getString("mijnWedstrijden", null) } returns null
            every {sharedPreferences.edit()} returns editor
            every {sharedPreferences.edit().putString(any(), any())} returns editor
            every{ editor.apply()} returns Unit
            coEvery{ userRepository.getMijnWedstrijden() } returns listOf(wed1, wed2)

            // Act
            runBlockingTest {
                val wedstrijden = repository.getMijnWedstrijden()
                // Assert
                Assert.assertEquals(2, wedstrijden!!.size)
                Assert.assertTrue(wedstrijden.contains(wed1))
                Assert.assertTrue(wedstrijden.contains(wed2))
                Assert.assertFalse(wedstrijden.contains(wed3))
            }
        }
}
