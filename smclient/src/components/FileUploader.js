import React, {useRef} from "react";


const FileUploader = ({onFileSelectError, onFileSelectSuccess}) => {
    const fileInput = useRef(null);

    const handleFileInput = (e) => {
        const file = e.target.files[0];
        console.log("Handle File: " + file.name);

        console.log("Size: " + file.size);

        if (file.size > 1000000) {
            onFileSelectError({ error: "File size cannot exceed more than 1MB" });
        }
        else {
            sessionStorage.setItem('img', URL.createObjectURL(file));
            onFileSelectSuccess(file);
        }
    }

    return (
        <div className="file-uploader">
            <input type="file" name="file" onChange={handleFileInput}/>
            <button onClick={e => fileInput.current && fileInput.current.click()} className="btn btn-primary"/>
        </div>
    )
}

export default FileUploader;