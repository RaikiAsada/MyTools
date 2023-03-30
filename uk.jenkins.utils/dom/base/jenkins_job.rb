# frozen_string_literal: true
class JenkinsJob
  attr_reader :jobName

  def self.run(rootDir, jobName)
    job = JenkinsJob.new(rootDir, jobName)

    if(job.exist?)
      yield(job)
    else
      puts "指定したジョブは存在しません:#{job.jobName}"
      return
    end

  end
  def initialize(rootDir, jobName)
    @rootDir = File.expand_path(rootDir)
    @jobName = jobName
  end

  def exist?
    return Dir.exist?(getJobPath)
  end

  def getJobPath
    return File.join(@rootDir, @jobName)
  end
end
