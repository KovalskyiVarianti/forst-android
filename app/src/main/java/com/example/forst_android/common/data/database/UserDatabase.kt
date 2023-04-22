package com.example.forst_android.common.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Database(
    entities = [
        ClusterDatabaseEntity::class
    ],
    exportSchema = false,
    version = 1
)
abstract class UserDatabase : RoomDatabase() {
    abstract fun clustersDao() : ClusterDao
}

@Dao
interface ClusterDao {

    @Query("SELECT * FROM clusters WHERE id=:id")
    fun getCluster(id: String) : ClusterDatabaseEntity

    @Query("SELECT * FROM clusters")
    fun getAllClusters() : Flow<List<ClusterDatabaseEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun createCluster(cluster: ClusterDatabaseEntity)

    @Query("SELECT (SELECT COUNT(*) FROM clusters) == 0")
    suspend fun isEmpty(): Boolean

    @Query("UPDATE clusters SET selected = (CASE WHEN id = :id THEN 1 ELSE 0 END) WHERE id != :id OR selected = 0")
    suspend fun selectCluster(id: String)

    @Query("SELECT * FROM clusters WHERE selected != 0")
    fun getSelectedCluster() : Flow<ClusterDatabaseEntity>

    @Transaction
    suspend fun createAndSelect(cluster: ClusterDatabaseEntity) {
        createCluster(cluster)
        selectCluster(cluster.id)
    }

}

@Entity(tableName = "clusters")
data class ClusterDatabaseEntity(
    @PrimaryKey val id: String,
    val name: String,
    @ColumnInfo(name = "selected") val isSelected : Boolean
)