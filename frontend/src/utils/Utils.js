import JSZip from "jszip";

export async function zipFile(file) {
  try {
    let zip = new JSZip();
    zip.file(file.name, file);
    const zipContent = await zip.generateAsync({ type: "blob" });
    return zipContent;
  } catch (err) {
    console.log(err);
  }
} 