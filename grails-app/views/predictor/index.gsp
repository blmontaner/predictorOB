<!DOCTYPE html>
<html>
	<head>
		<g:javascript src="jquery.js"/>
		<g:javascript src="jquery.form.min.js"/>
		 <link rel="stylesheet" href="${resource(dir: 'css', file: 'bootstrap.min.css')}" type="text/css">
		<title> Predictor </title>
	</head>
	<body>
	<div class="container">

		<div class="row">
          <div class="col-lg-12">
            <div class="page-header">
              <g:if test="${message != null}" >	
              	<p class="alert alert-dismissable alert-success">${message}</p>
              	</g:if>
        	</div>
            <div class="bs-component">
              <div class="jumbotron">
                <h1>Predictor</h1>
				<p class="lead">Bienvenido a predictor, el sitio donde podra predecir eventos futuros.</p>	
				</div>
                <p><p/>
                
				
				
              </div>
              <g:form action="predict">
  			 	  <h3>Fecha del evento:</h3>
		          <g:datePicker name="date" value="${date}" noSelection="['':'-Elegir-']" class="btn btn-info"/>
		          <h3> Elija uno de los modelos cargados para realizar una predicción <a href="#" onclick="jQuery('#fileUploader').show()" class="btn btn-warning">+</a></h3>
		       
		         
		          <table class="table table-striped table-hover ">
					  <thead>
					    <tr>
					      <th>id</th>
					      <th>Modelo</th>
					    </tr>
					  </thead>
					  <tbody>
		           		<g:each in="${modelList}" var="model">		
			           		<g:if test="${model.modelReadyForPredictions}" >		       	
						        <tr id="tableRow${model.id}" onclick="selectRadioButton(${model.id})" class="tableRow">
							      <td>${model.id}</td>
							      <td>${model.filename}</td>
							      <td><input type="radio" name="optionsRadios" id="optionsRadios${model.id}" value="${model.id}" ></td>						      
							    </tr>
						    </g:if>			
				   		</g:each>

				   </tbody>
				   </table>
				   <g:hiddenField name="modelId" value="1"/>
				   <g:submitButton name="predict" value="Predecir" class="btn btn-success"/>
			   </g:form>


			  
            <div id="source-button" class="btn btn-primary btn-xs" style="display: none;">&lt; &gt;</div></div>
            
          </div>
        
        </br	>

        <p id="predictionResult" class="alert alert-dismissable alert-info">RESULTADO: ${predictionPlace.predictionString}</p>
        <p></p>
		<div style="width: 600px;height: 450px;margin: 0 auto;">
			<iframe
			  width="600"
			  height="450"
			  frameborder="0" style="border:0;margin auto:0"
			  src="https://www.google.com/maps/embed/v1/search?key=AIzaSyBaL9EOahkGyQznuEstxpKx3XKXGD0jCeY&q=${predictionPlace.predictionStringAddress},URUGUAY">
			</iframe> 
		<div>
		</br>
		</br>
			<div class="modal" id="fileUploader">
				  <div class="modal-dialog">
				    <div class="modal-content">
				      <div class="modal-header">
				        <button type="button" class="close" data-dismiss="modal" aria-hidden="true" onclick="jQuery('#fileUploader').hide()">×</button>
				        <h4 class="modal-title">Suba el archivo de eventos pasados</h4>
				      </div>
				      <g:uploadForm class="form-horizontal" action='saveFile' name="myUpload" >
					      <div class="modal-body">
					        <p>El archivo debe ser de formato csv</p>
					      </div>
					      <div class="modal-footer">

					    <input type="file" name="file" class="btn btn-primary btn-lg label-default" required="true"/>
					    <g:submitButton name="upload" class="btn btn-primary btn-lg" data-dismiss="modal" value="Upload" />
					
				        </g:uploadForm>
				      </div>
				    </div>
				  </div>
</div>
</div>
		<script> 
		function selectRadioButton(rbid){
			jQuery('.tableRow').removeClass('warning');
			jQuery('#tableRow'+rbid).addClass('warning');
			jQuery('#optionsRadios'+rbid).attr('checked', true);
			jQuery("input[name='modelId']").val(rbid);		  
		}
		</script> 
	</body>
