package com.upao.panaderia.models.responseModel

import com.google.gson.annotations.SerializedName

data class CategoryResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: String,
    @SerializedName("parent_id") val parent_id: Int,
    @SerializedName("position") val position: Int,
    @SerializedName("status") val status: Int,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("updated_at") val updated_at: String,
    @SerializedName("priority") val priority: Int,
    @SerializedName("slug") val slug: String,
    @SerializedName("products_count") val products_count: Int
)
{
    override fun toString(): String {
        return "CategoryResponse(id=$id, name='$name', image='$image', parent_id=$parent_id, position=$position, status=$status, created_at='$created_at', updated_at='$updated_at', priority=$priority, slug='$slug', products_count=$products_count)"
    }
}