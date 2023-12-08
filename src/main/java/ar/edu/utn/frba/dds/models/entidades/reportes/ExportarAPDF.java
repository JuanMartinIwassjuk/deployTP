package ar.edu.utn.frba.dds.models.entidades.reportes;

public class ExportarAPDF implements EstrategiaDeExportacion{

    private AdapterExportadorPDF adapter;

    public ExportarAPDF(AdapterExportadorPDF adapter){
      this.adapter = adapter;
    }

    public String exportar(Exportable exportable) {
      return this.adapter.exportar(exportable);
    }

}
