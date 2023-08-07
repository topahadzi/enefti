import com.example.enefti.data.remote.model.Collections
import com.example.enefti.data.remote.model.TopCollections
import com.google.gson.annotations.SerializedName

data class TopCollectionsList(
    @SerializedName("results") val mList : List<TopCollections>

    )