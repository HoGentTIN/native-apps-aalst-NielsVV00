package com.example.project3pt

import com.example.project3pt.fragments.maakWedstrijd.MaakWedstrijdViewModel
import com.example.project3pt.repositories.WedstrijdRepository
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.*

class MaakWedstrijdViewModelTest{
    private val wedstrijdRepository: WedstrijdRepository = mockk()
    private lateinit var viewModel: MaakWedstrijdViewModel

    @Before
    fun setUp() {
        viewModel = MaakWedstrijdViewModel(wedstrijdRepository)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun doPost() {
        // Arrange
        // Act
        runBlockingTest {
            viewModel.saveDate(2024, 6, 15)
            viewModel.postWedstrijd("test", "test")
            // Assert
            coVerify { wedstrijdRepository.postWedstrijd(any()) }
        }
    }
}
