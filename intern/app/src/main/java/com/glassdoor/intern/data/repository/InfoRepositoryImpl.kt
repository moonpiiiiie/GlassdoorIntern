/*
 * Copyright (c) 2023, Glassdoor Inc.
 *
 * Licensed under the Glassdoor Inc Hiring Assessment License.
 * You may not use this file except in compliance with the License.
 * You must obtain explicit permission from Glassdoor Inc before sharing or distributing this file.
 * Mention Glassdoor Inc as the source if you use this code in any way.
 */

package com.glassdoor.intern.data.repository

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.glassdoor.intern.data.mapper.HeaderInfoMapper
import com.glassdoor.intern.data.model.ItemInfoDto
import com.glassdoor.intern.data.source.InfoApi
import com.glassdoor.intern.domain.model.HeaderInfo
import com.glassdoor.intern.domain.repository.InfoRepository
import timber.log.Timber
import javax.inject.Inject

/**
 * TODO: Inject the correct mapper dependency
 */
internal class InfoRepositoryImpl @Inject constructor(
    private val infoApi: InfoApi,
    private val headerInfoMapper: HeaderInfoMapper,
    private val items: List<ItemInfoDto>
) : InfoRepository {
    override suspend fun getHeaderInfo(): Result<HeaderInfo, Throwable> =
        try {
            with(infoApi.getInfo()) {
                when {
                    header != null -> Ok(headerInfoMapper.toDomain(header, items))// Convert DTO into domain model HeaderInfoDto to HeaderInfo
                    error != null -> Err(Exception(error))// Exception extends Throwable
                    else -> Err(Exception("Unknown error occurred"))
                }
            }
        } catch (throwable: Throwable) {
            Timber.e(throwable, "InfoRepositoryImpl")
            Err(throwable)
        }
}
