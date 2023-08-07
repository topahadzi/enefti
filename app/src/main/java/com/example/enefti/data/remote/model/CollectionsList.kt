import com.example.enefti.data.remote.model.Collections
import com.google.gson.annotations.SerializedName

data class CollectionsList(
    @SerializedName("results") val mList : List<Collections>

    )