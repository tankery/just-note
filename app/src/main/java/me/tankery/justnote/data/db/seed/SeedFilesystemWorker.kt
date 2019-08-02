package me.tankery.justnote.data.db.seed

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import me.tankery.justnote.utils.RES_SAMPLE_NOTES_DIR
import me.tankery.justnote.utils.copyDirOrFile
import me.tankery.justnote.utils.getNotesRoot
import timber.log.Timber

/**
 * Seeding filesystem and note database with prepared sample notes.
 */
class SeedFilesystemWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result =
        try {
            val ctx = applicationContext
            val dest = getNotesRoot(ctx)
            val copied = ctx.assets.copyDirOrFile(RES_SAMPLE_NOTES_DIR, dest)

            Timber.i("Seeding filesystem with %d files", copied)
            Result.success()
        } catch (ex: Exception) {
            Timber.e(ex, "Error seeding filesystem")
            Result.failure()
        }
}
