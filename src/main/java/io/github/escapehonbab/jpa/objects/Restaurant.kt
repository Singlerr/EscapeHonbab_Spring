package io.github.escapehonbab.jpa.objects

import io.github.escapehonbab.jpa.model.BaseModel
import lombok.Builder
import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.Table

@Builder
@Table(name = "restaurants")
@Entity
class Restaurant(
    val name: String,
    val rate: Double,
    val category: String,
    val address: String,
    val image: ByteArray
) : Serializable, BaseModel()