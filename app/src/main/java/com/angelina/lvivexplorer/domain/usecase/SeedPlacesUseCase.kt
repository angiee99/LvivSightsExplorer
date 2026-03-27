package com.angelina.lvivexplorer.domain.usecase

import com.angelina.lvivexplorer.domain.repository.PlaceRepository
import javax.inject.Inject

class SeedPlacesUseCase @Inject constructor(
    private val placeRepository: PlaceRepository
) {
    suspend operator fun invoke() {
        placeRepository.seedIfNeeded()
    }
}
