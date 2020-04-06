package io.autorune.client.instance

import org.objectweb.asm.ClassWriter
import org.objectweb.asm.tree.ClassNode
import java.awt.AWTPermission
import java.io.*
import java.net.SocketPermission
import java.net.URL
import java.security.*
import java.util.*


class ClientInstanceClassLoader(file: File, classNodes: ArrayList<ClassNode>, codebase: String) : ClassLoader(ClientInstanceClassLoader::class.java.classLoader)
{

    private val classes = Hashtable<String, Class<*>>()

    private val classBytes = hashMapOf<String, ByteArray>()

    private var domain: ProtectionDomain? = null

    private var directoryPath = ""

    init {

        println("Initialized ClassLoader!")

        directoryPath = file.path.substring(0, file.path.lastIndexOf("\\") + 1)

        domain = try {
            val codeSource = CodeSource(URL(codebase), null as Array<CodeSigner>?)
            ProtectionDomain(codeSource, getPermissions())
        } catch (e: Exception) {
            ProtectionDomain(null, getPermissions())
        }

        try {
            for (classNode in classNodes) {
                val writer = ClassWriter(ClassWriter.COMPUTE_MAXS)
                classNode.accept(writer)
                classNode.visitEnd()
                val data = writer.toByteArray()
                classBytes[classNode.name + ".class"] = data
                //System.out.println("Loaded : "+classNode.name+" "+data.size);
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getPermissions(): Permissions {

        val permissions = Permissions()

        permissions.add(AWTPermission("accessEventQueue"))
        permissions.add(PropertyPermission("user.home", "read"))
        permissions.add(PropertyPermission("java.vendor", "read"))
        permissions.add(PropertyPermission("java.version", "read"))
        permissions.add(PropertyPermission("os.name", "read"))
        permissions.add(PropertyPermission("os.arch", "read"))
        permissions.add(PropertyPermission("os.version", "read"))
        permissions.add(SocketPermission("*", "connect,resolve"))

        var homeDir: String? = System.getProperty("user.home")

        if (homeDir != null)
        {
            homeDir += "/"
        } else {
            homeDir = "~/"
        }

        val dirs = arrayOf("c:/rscache/", "/rscache/", "c:/windows/", "c:/winnt/", "c:/", homeDir, "/tmp/", ".")
        val rsDirs = arrayOf(".jagex_cache_32", ".file_store_32")

        for (dir in dirs)
        {
            val file = File(dir)

            permissions.add(FilePermission(dir, "read"))

            if (!file.exists()) {
                continue
            }

            val newDir = file.path

            for (rsDir in rsDirs) {
                permissions.add(FilePermission(newDir + File.separator + rsDir + File.separator + "-", "read"))
                permissions.add(FilePermission(newDir + File.separator + rsDir + File.separator + "-", "write"))
            }
        }

        Calendar.getInstance()

        permissions.setReadOnly()

        return permissions
    }

    @Throws(ClassNotFoundException::class)
    override fun loadClass(name: String): Class<*>
    {
        try
        {
            val entryName = name.replace('.', '/') + ".class"

            if (classes.containsKey(name))
                return classes.getValue(name)

            if (this.classBytes.containsKey(entryName))
            {
                val buf = this.classBytes[entryName]
                val clazz = defineClass(name, buf, 0, buf?.size ?: 0, domain)
                classes[name] = clazz
                return clazz
            }
            val temp = File(directoryPath + name.replace(".", "\\") + ".class")
            if (temp.exists()) {
                val stream = FileInputStream(temp)
                val bytes = ByteArray(stream.available())
                stream.read(bytes)
                stream.close()
                classBytes[name] = bytes
                val buf = this.classBytes[entryName]
                val clazz = defineClass(name, buf, 0, buf?.size ?: 0, null)
                if (clazz != null) {
                    classes[entryName] = clazz
                    return clazz
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } catch (t: Throwable) {
            t.printStackTrace()
        }

        try {
            return this.javaClass.classLoader.loadClass(name)
        } catch (e: Exception) {
            e.printStackTrace()
        } catch (t: Throwable) {
            t.printStackTrace()
        }

        return super.loadClass(name)
    }

}