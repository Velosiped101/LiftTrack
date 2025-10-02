package com.velosiped.datastore.implementation

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import com.velosiped.notes.data.datastore.AppProtoStore
import java.io.InputStream
import java.io.OutputStream

internal object ProgramTempProgressSerializer: Serializer<AppProtoStore> {
    override val defaultValue: AppProtoStore
        get() = AppProtoStore.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): AppProtoStore {
        return try {
            AppProtoStore.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException(EXCEPTION_MESSAGE, e)
        }
    }

    override suspend fun writeTo(t: AppProtoStore, output: OutputStream) {
        t.writeTo(output)
    }
}

private const val EXCEPTION_MESSAGE = "Cannot read proto"
private const val APP_PROTO_STORE_NAME = "app_proto_store.pb"

internal val Context.appProtoDataStore: DataStore<AppProtoStore> by dataStore(
    fileName = APP_PROTO_STORE_NAME,
    serializer = ProgramTempProgressSerializer
)