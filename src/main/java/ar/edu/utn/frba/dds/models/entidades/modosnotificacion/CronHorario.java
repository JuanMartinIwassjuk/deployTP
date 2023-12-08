package ar.edu.utn.frba.dds.models.entidades.modosnotificacion;


import ar.edu.utn.frba.dds.models.entidades.comunidades.Miembro;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioMiembros;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import java.util.List;

public class CronHorario implements Job {
  public static void iniciarNotificador() {

    JobDetail notificacionJob = JobBuilder.newJob(CronHorario.class)
        .withIdentity("notiJob", "group1")
        .build();

    Trigger notificacionTrigger = TriggerBuilder.newTrigger()
        .withIdentity("notiTrigger", "group1")
        .withSchedule(CronScheduleBuilder.cronSchedule("0 0/5 * * * ?"))
        .build();

    Scheduler scheduler;
    try {
      scheduler = new StdSchedulerFactory().getScheduler();
      scheduler.start();
      scheduler.scheduleJob(notificacionJob, notificacionTrigger);
    } catch (SchedulerException e) {
      e.printStackTrace();
    }

  }

  @Override
  public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    RepositorioMiembros repositorioMiembros = new RepositorioMiembros();
    List<Miembro> todosLosMiembros = repositorioMiembros.buscarTodos();
    todosLosMiembros.forEach(Miembro::notificarPendientes);
    //RepositorioMiembros.getInstance().buscarTodos().forEach(Miembro::notificarPendientes);
  }


}