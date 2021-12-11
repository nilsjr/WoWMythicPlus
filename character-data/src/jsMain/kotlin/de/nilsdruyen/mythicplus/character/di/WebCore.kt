package de.nilsdruyen.mythicplus.character.di

import de.nilsdruyen.mythicplus.character.CharacterUsecase
import de.nilsdruyen.mythicplus.character.CharacterUsecaseImpl
import de.nilsdruyen.mythicplus.character.RaiderIoRepositoryImpl
import de.nilsdruyen.mythicplus.character.apis.WebApiImpl
import de.nilsdruyen.mythicplus.character.apis.applyDefaultConfig
import io.ktor.client.HttpClient
import io.ktor.client.features.BrowserUserAgent

fun createUsecase(): CharacterUsecase = CharacterUsecaseImpl(RaiderIoRepositoryImpl(WebApiImpl(createClient())))

fun createClient(): HttpClient = HttpClient {
  applyDefaultConfig("raider.io/api/v1")
  BrowserUserAgent()
}